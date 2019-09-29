package io.appfirewall.fx.ui.linux;

import com.google.common.flogger.FluentLogger;

/**
 * This class starts the collection of several information pieces we want to display to the user,
 * but are not delivered by the AppFirewall daemon directly.
 * E.g. Information from the .linux file, the app icon, the username for a UID.
 */
public class ConnectionInfoCollector implements Runnable {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    @Override
    public void run() {
        try {
            var count = LinuxDesktopParser.collectAllAppInfos();
            logger.atInfo().log("Collected %d Desktop files", count);
            IconLocator.getIconForName(LinuxDesktopParser.DEFAULT_ICON); // Cache default icon
        } catch (Exception ex) {
            logger.atWarning().withCause(ex).log("Exception in ConnectionInfoCollectorThread");

        }

    }
}
