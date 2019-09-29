package io.appfirewall.fx.ui.statistics;

import com.google.common.flogger.FluentLogger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import io.appfirewall.fx.ui.desktop.ExceptionDialog;
import io.appfirewall.fx.ui.Main;

import java.net.URL;

public class Statistics {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public void open() {
        try {
            URL resource = getClass().getResource("/statistics.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(resource);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 780, 500);

            var stage = new Stage();
            stage.setTitle(Main.APP_NAME + " Network Statistics");

            stage.setScene(scene);

            stage.show();
        } catch (Exception ex) {
            logger.atSevere().withCause(ex).log();
            new ExceptionDialog().showException(ex);
        }
    }
}
