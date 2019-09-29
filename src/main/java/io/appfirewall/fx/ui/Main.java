package io.appfirewall.fx.ui;

import com.google.common.flogger.FluentLogger;
import io.appfirewall.fx.ui.communication.UnixSocketGrpcServer;
import io.appfirewall.fx.ui.desktop.ExceptionDialog;
import io.appfirewall.fx.ui.desktop.TrayEntry;
import io.appfirewall.fx.ui.linux.ConnectionInfoCollector;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static final String APP_NAME = "AppFirewall FX";
    public static final String DEFAULT_UNIX_SOCKET = "/tmp/appfirewall.sock"; // without unix://
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public static void main(String[] args) {
        try {
            Platform.setImplicitExit(false);
            Application.launch(args);
        } catch (Exception ex) {
            logger.atSevere().withCause(ex).log();
            new ExceptionDialog().showException(ex);
        }
    }

    @Override
    public void start(Stage stage) {

        try {
            createTrayIcon(); // Wait for newer version
            var connectionInfoCollectorThread = new Thread(new ConnectionInfoCollector());
            connectionInfoCollectorThread.start();

            startGrpcServer();
        } catch (Exception ex) {
            logger.atSevere().withCause(ex).log();
            new ExceptionDialog().showException(ex);
        }
    }

    private void createTrayIcon() {
        var trayEntry = new TrayEntry();
        trayEntry.show();
    }

    private void startGrpcServer() throws IOException {
        final var server = new UnixSocketGrpcServer();
        server.start(DEFAULT_UNIX_SOCKET);
        //server.blockUntilShutdown();
    }
}