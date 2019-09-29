package io.appfirewall.fx.ui.desktop;

import com.google.common.flogger.FluentLogger;
import io.appfirewall.fx.ui.statistics.Statistics;
import javafx.application.Platform;
import io.appfirewall.fx.ui.Main;

import javax.swing.*;
import java.awt.*;

public class TrayEntry {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public TrayEntry() {
        // TODO wait for newer version
        // See issue: https://github.com/dorkbox/SystemTray/issues/95
    }

    public void show() {
        //checking for support

        Image icon = null;
        try {
            icon = new ImageIcon(this.getClass().getResource("/icon_tray.png")).getImage();
        } catch (Exception ex) {
            logger.atSevere().withCause(ex).log("Could not load tray icon");
            return;
        }
        if (!SystemTray.isSupported()) {
            logger.atFine().log("System tray is not supported");
            return;
        }
        SystemTray systemTray = SystemTray.getSystemTray();

        PopupMenu trayPopupMenu = new PopupMenu();

        MenuItem action = new MenuItem("Statistics");
        action.addActionListener(event ->
        {
            Platform.runLater(() -> {
                var statistics = new Statistics();
                statistics.open();
            });
        });
        trayPopupMenu.add(action);

        MenuItem close = new MenuItem("Quit");
        close.addActionListener(event -> System.exit(0));
        trayPopupMenu.add(close);

        TrayIcon trayIcon = new TrayIcon(icon, Main.APP_NAME, trayPopupMenu);
        trayIcon.setImageAutoSize(true);

        try {
            systemTray.add(trayIcon);
        } catch (AWTException ex) {
            logger.atSevere().withCause(ex).log("Could not add tray icon.");
        }
    }
}
