package io.appfirewall.fx.ui.linux;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lookup different paths to search for icons
 * A matching icon is a nice to have and not extremely important for the functioning of AppFirewall FX.
 *
 * Icons are referenced in the applications .linux files and stored in different paths:
 * e.g. VS Code's Icon is currently in /usr/share/pixmaps/ as com.visualstudio.code.png
 * e.g. Firefox's Icons is currently in /usr/share/icons/hicolor/48x48/apps/ as firefox.png
 * e.g. Terminal's Icon is /usr/share/icons/gnome/48x48/apps/ as terminal.png (For Gnome)
 * At first we only want to support PNG as JavaFX doesn't support SVG natively
 * It would be easier if there would already exist an Icon search strategy in Java
 * or if we could use the GTK+ Icon cache as Qt does:
 * https://codereview.qt-project.org/#/c/125379/
 * https://code.woboq.org/qt5/qtbase/src/gui/image/qiconloader.cpp.html
 *
 * Get current icon theme:
 * Gnome: gsettings get org.gnome.linux.interface icon-theme
 */
public class IconLocator {
    private final static List<String> searchPaths = new ArrayList<>();
    private final static Map<String, Optional<Path>> iconPathCache = new ConcurrentHashMap<>();
    private final static String PATH_PART = "48x48/apps/";
    private final static String EXTENSION = ".png";

    static {
        // TODO maybe go over all folders in /usr/share/icons/

        searchPaths.add("/usr/share/icons/hicolor/" + PATH_PART);
        searchPaths.add("/usr/share/pixmaps/");
        searchPaths.add("/usr/share/icons/gnome/" + PATH_PART);
    }

    /**
     * Tries to find an icon for the given name by looking through different paths.
     * @param iconName a name as found in the .linux files (e.g. terminal)
     * @return a path pointing to the image or null
     */
    public static Optional<Path> getIconForName(String iconName) {
        var imagePath = iconPathCache.get(iconName);
        //noinspection OptionalAssignedToNull
        if (imagePath != null) {
            return imagePath;
        }
        return searchIcon(iconName);
    }

    private static Optional<Path> searchIcon(String iconName) {
        for (var searchPath : searchPaths) {
            var path = Path.of(searchPath, iconName + EXTENSION);
            if (Files.exists(path)) {
                var imagePath = Optional.of(path);
                iconPathCache.putIfAbsent(iconName, imagePath);
                return imagePath;
            }
        }
        iconPathCache.putIfAbsent(iconName, Optional.empty());
        return Optional.empty();
    }

}
