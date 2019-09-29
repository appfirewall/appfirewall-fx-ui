package io.appfirewall.fx.ui.prompt;

import com.google.common.flogger.FluentLogger;
import io.appfirewall.protocol.AFConnectionInfo;
import io.appfirewall.protocol.AFRule;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import io.appfirewall.fx.ui.desktop.ExceptionDialog;
import io.appfirewall.fx.ui.Main;

import java.net.URL;
import java.util.Optional;

public class Prompt {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
    private final AFConnectionInfo request;
    private double xOffset;
    private double yOffset;

    public Prompt(AFConnectionInfo request) {
        this.request = request;
    }

    public Optional<AFRule> open() {
        try {
            URL resource = getClass().getResource("/prompt.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(resource);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 600, 272);
            scene.setFill(Color.TRANSPARENT);

            var stage = new Stage();
            stage.setTitle(Main.APP_NAME);
            //stage.initStyle(StageStyle.UTILITY);

            //stage.initStyle(StageStyle.UTILITY);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.setResizable(false);
            //stage.initModality(Modality.APPLICATION_MODAL);
            stage.setAlwaysOnTop(true);

            scene.setOnMousePressed(event -> {
                xOffset = stage.getX() - event.getScreenX();
                yOffset = stage.getY() - event.getScreenY();
            });
            scene.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() + xOffset);
                stage.setY(event.getScreenY() + yOffset);
            });

            var controller = fxmlLoader.<PromptController>getController();
            controller.setConnection(request);

            stage.showAndWait();
            var rule = controller.getRule();
            return Optional.ofNullable(rule);
        } catch (Exception ex) {
            logger.atSevere().withCause(ex).log();
            new ExceptionDialog().showException(ex);
        }
        return Optional.empty();
    }
}
