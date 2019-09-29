package io.appfirewall.fx.ui.linux;

import com.google.common.base.Splitter;
import com.google.common.flogger.FluentLogger;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * We read the users from /etc/passwd
 * Unfortunately, Java does not provide a method to get a username for a UID.
 */
// TODO delete
public class UserParser {
    private static final Map<Long, String> uidUsernameCache = new ConcurrentHashMap<>();
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public static String getFormattedUserForUid(String uid) {
        try {
            long parsed = Long.parseLong(uid);
            return getFormattedUserForUid(parsed);
        } catch (NumberFormatException ex) {
            return uid;
        }
    }

    public static String getFormattedUserForUid(long uid) {
        var user = getUsernameForUid(uid);
        if (user.isPresent()) {
            return uid + " (" + user.get() + ")";
        }
        return uid + "";
    }

    public static Optional<String> getUsernameForUid(long uid) {
        var username = uidUsernameCache.get(uid);
        if (username != null) {
            return Optional.of(username);
        }
        return Optional.empty();
    }


    public static int collectUsers() {
        // Format is like this:
        // root:x:0:0: ...
        // <username>:<x : password stored in shadow>:<uid>:<gid>: ...
        try {
            var content = new String(Files.readAllBytes(Path.of("/etc/passwd")), Charset.forName("UTF-8"));
            parseFileContent(content);
        } catch (Exception ex) {
            logger.atWarning().withCause(ex).log("Error while collecting users from /etc/passwd");
        }
        return uidUsernameCache.size();
    }

    static void parseFileContent(String content) {
        content.lines().forEach(line -> {
            var parts = Splitter.on(":").splitToList(line);
            if (parts.size() > 4) {
                try {
                    long uid = Long.parseLong(parts.get(2));
                    var username = parts.get(0);
                    uidUsernameCache.put(uid, username);
                } catch (NumberFormatException ex) {
                    // We are not interested in the user if we cannot parse the UID.
                    logger.atFinest().withCause(ex).log("Could not parse UID");
                }
            }
        });
    }
}
