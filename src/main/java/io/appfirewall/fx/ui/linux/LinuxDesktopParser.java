package io.appfirewall.fx.ui.linux;

import com.google.common.flogger.FluentLogger;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * LinuxDesktopParser extracts the information from the .linux files
 */
public class LinuxDesktopParser {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
    private static final Map<String, AppInfo> appInfoCache = new ConcurrentHashMap<>();
    static final String DEFAULT_ICON = "terminal";
    private static final Map<String, String> FIXES = new HashMap<>();
    private static List<String> pathEntries;

    static {
        // Fixes for the resolution, taken from: desktop_parser.py
        FIXES.put("/opt/google/chrome/chrome", "/opt/google/chrome/google-chrome");
        FIXES.put("/usr/lib/firefox/firefox", "/usr/lib/firefox/firefox.sh");
        FIXES.put("/usr/bin/pidgin.orig", "/usr/bin/pidgin");
    }

    public static AppInfo getInfoByPath(String path) {
        logger.atFine().log("Info for %s", path);
        var fixedPath = FIXES.get(path);
        if (fixedPath != null) {
            path = fixedPath;
        }
        var appInfo = appInfoCache.get(path);
        logger.atFine().log("Info found %s", appInfo);
        if (appInfo != null) {
            return appInfo;
        }
        return new AppInfo(getExecutableName(path), DEFAULT_ICON);
    }

    private static String getExecutableName(String path) {
        var parts = path.split(File.separator);
        return parts[parts.length - 1];
    }

    public static int collectAllAppInfos() {
        // TODO write test that passes in different data dirs
        var desktopDataDirs = getDesktopDataDirs();
        logger.atFine().log("Found following desktop data dirs: %s", desktopDataDirs.toString());
        pathEntries = getPathEntries();
        for (var dataDir : desktopDataDirs) {
            var path = Path.of(dataDir);
            if (Files.exists(path)) {
                logger.atInfo().log(path.toString());
                collectInfoFromDesktopFiles(path);
            }
        }
        return appInfoCache.size();
    }

    private static Set<String> getDesktopDataDirs() {
        final var APP_DIR = "/applications";
        var dataDirs = System.getenv("XDG_DATA_DIRS");
        if (dataDirs == null) {
            return Set.of("/usr/share" + APP_DIR);
        }
        return Arrays.stream(dataDirs.split(File.pathSeparator))
                .map((path) -> path + APP_DIR)
                .collect(Collectors.toSet());
    }

    private static List<String> getPathEntries() {
        var path = System.getenv("PATH");
        if (path == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(path.split(File.pathSeparator))
                .collect(Collectors.toList());
    }

    private static void collectInfoFromDesktopFiles(Path desktopDataDir) {
        logger.atFine().log("Search in: %s", desktopDataDir);
        try {
            Files.walk(desktopDataDir, FileVisitOption.FOLLOW_LINKS)
                    .filter((filename) -> filename.toString().endsWith(".desktop"))
                    .forEach((path) -> collectInfoFromDesktopFile(path));
        } catch (Exception ex) {
            logger.atWarning().withCause(ex).log("Could not collect desktop files from folder: %s", desktopDataDir.toString());
        }
    }

    private static void collectInfoFromDesktopFile(Path path) {
        logger.atFine().log("Collecting from: %s", path.toAbsolutePath());
        try {
            Wini ini = new Wini(path.toFile());

            var exec = ini.get("Desktop Entry", "Exec");
            if (exec == null) {
                return;
            }

            var name = ini.get("Desktop Entry", "Name");
            if (name == null) {
                return;
            }

            var icon = ini.get("Desktop Entry", "Icon");

            if (icon == null) {
                icon = DEFAULT_ICON;
            }
            var appInfo = new AppInfo(name, icon);

            parseAndLocateExec(exec).ifPresent((cleanResolvedExec) -> {
                appInfoCache.put(cleanResolvedExec.toString(), appInfo);
                logger.atFine().log("resolved %s", cleanResolvedExec.toString());

                if (Files.isSymbolicLink(cleanResolvedExec)) {
                    // put both variants into the map
                    try {
                        appInfoCache.put(cleanResolvedExec.toRealPath().toString(), appInfo);
                    } catch (IOException ex) {
                        logger.atFine().log("Could not resolve symlink %s", cleanResolvedExec.toString());
                    }
                }
            });
        } catch (Exception ex) {
            logger.atWarning().withCause(ex).log("Could not parse %s file", path.getFileName().toString());
        }
    }

    private static Optional<Path> parseAndLocateExec(String exec) {
        var cleanExec = cleanExec(exec);
        if (cleanExec.length() == 0) {
            logger.atWarning().log("We might have cleaned our exec %s too much", exec);
            return Optional.empty();
        }
        if (cleanExec.charAt(0) == '/') {
            return Optional.of(Path.of(cleanExec));
        }
        for (var pathEntry : pathEntries) {
            var path = Path.of(pathEntry, cleanExec);
            if (Files.exists(path)) {
                return Optional.of(path);
            }
        }

        return Optional.empty();
    }

    static String cleanExec(String exec) {
        // remove stuff like %U
        var cleanExec = exec.replaceAll("%[a-zA-Z]+", "");

        // remove 'env .... command'
        cleanExec = cleanExec.replaceAll("^env\\s+[^\\s]+\\s", "");

        // split && strip
        cleanExec = cleanExec.split(" ")[0].strip();

        // remove quotes
        cleanExec = cleanExec.replaceAll("[\"\']+", "");

        return cleanExec;
    }
}
