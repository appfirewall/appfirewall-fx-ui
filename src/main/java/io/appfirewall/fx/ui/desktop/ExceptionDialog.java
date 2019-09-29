package io.appfirewall.fx.ui.desktop;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import io.appfirewall.fx.ui.Main;

public class ExceptionDialog {
	public void showException(Exception ex) {
		showException(ex, null);
	}

	public void showException(Exception ex, String msg) {
		Platform.runLater(() -> showExceptionWindow(ex, msg));
	}

	private void showExceptionWindow(Exception ex, String msg) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(Main.APP_NAME + " Exception");
		alert.setHeaderText("An exception occured");
		alert.setContentText(ex.getMessage());
		if(msg != null && !msg.isEmpty()){
			alert.setHeaderText("An exception occured" + "\n" + msg);
		}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("The exception stacktrace was:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}
}
