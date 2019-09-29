package io.appfirewall.fx.ui.prompt;

import com.google.common.base.Joiner;
import com.google.common.flogger.FluentLogger;
import io.appfirewall.protocol.AFConnectionInfo;
import io.appfirewall.protocol.AFOperator;
import io.appfirewall.protocol.AFRule;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import io.appfirewall.fx.ui.linux.IconLocator;
import io.appfirewall.fx.ui.linux.LinuxDesktopParser;
import io.appfirewall.fx.ui.linux.UserParser;
import io.appfirewall.fx.ui.statistics.Statistics;
import io.appfirewall.fx.ui.util.DomainUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class PromptController implements Initializable {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private AFConnectionInfo con;
    private AFRule rule;

    private String mainDstDomain;

    @FXML
    private Label appNameLabel;

    @FXML
    private ImageView appIcon;

    @FXML
    private TextFlow message;

    @FXML
    private Label destASInfoLabel;

    @FXML
    private Label destIPLabel;

    @FXML
    private Label uidLabel;

    @FXML
    private Label pidLabel;

    @FXML
    private Label argsLabel;

    @FXML
    private Button denyButton;

    @FXML
    private Button allowButton;

    @FXML
    private ComboBox<Operator> operatorCombo;

    @FXML
    private ComboBox<Duration> durationCombo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allowButton.setOnAction((event) -> createRuleAndClose(Action.ALLOW));
        denyButton.setOnAction((event) -> createRuleAndClose(Action.DENY));
    }

    private void createRuleAndClose(Action action) {

        var operator = createOperator();
        var data = operator.getData().replace("/", "-"); // daemon uses name for filename
        var name = String.format("%s-%s-%s-fx", action.getValue(), operator.getType(), data);

        this.rule = AFRule.newBuilder()
                .setAction(action.getValue())
                .setDuration(durationCombo.getValue().getTitle())
                .setOperator(operator)
                .setName(name)
                .build();

        logger.atInfo().log("new rule: " + name);

        var stage = (Stage) appNameLabel.getScene().getWindow();
        stage.close();
    }

    private AFOperator createOperator() {
        var data = "";

        var operatorValue = operatorCombo.getValue();

        // taken from prompt.py
        switch (operatorValue.getOperand()) {
            case USER_ID:
                data = "" + con.getUserId();
                break;
            case DEST_IP:
                data = con.getDstIp();
                break;
            case DEST_HOST:
                data = con.getDstHost();
                break;
            case DEST_MAIN_HOST:
                data = DomainUtil.createDaemonRegex(mainDstDomain);
                break;
        }

        return AFOperator.newBuilder()
                .setType(operatorValue.getType().getValue())
                .setOperand(operatorValue.getOperand().getValue())
                .setData(data)
                .build();
    }

    private void setupRequest() {
        if (con == null) {
            return;
        }

        var appInfo = LinuxDesktopParser.getInfoByPath(con.getProcessPath());
        appNameLabel.setText(appInfo.getAppName());

        IconLocator.getIconForName(appInfo.getAppIcon()).ifPresent(
                (iconPath) -> {
                    Image image = new Image(iconPath.toUri().toString(), 48, 48, true, true);
                    appIcon.setImage(image);
                }
        );

        appIcon.setOnMouseClicked((e) -> {
            // TODO temporarily open statistics
            var statistics = new Statistics();
            statistics.open();
        });

        var args = Joiner.on(" ").skipNulls().join(con.getProcessArgsList());
        argsLabel.setText(con.getProcessPath() + " " + args);
        argsLabel.setTooltip(new Tooltip(args));


        Text appNameText = new Text(appInfo.getAppName());
        appNameText.setStyle("-fx-font-weight: bold");

        Text connectingToText = new Text(" is trying to connect to ");

        Text hostNameText = new Text(con.getDstHost());
        hostNameText.setStyle("-fx-font-weight: bold");

        message.getChildren().addAll(appNameText, connectingToText, hostNameText);

        destASInfoLabel.setText(con.getDstAsName() + " (AS" + con.getDstAsNumber() + ")");
        destIPLabel.setText(con.getDstIp() + ":" + con.getDstPort());
        uidLabel.setText(con.getUserName() + " (" + con.getUserId() + ")");
        pidLabel.setText("" + con.getProcessId());

        setupWhatCombo();

        durationCombo.getItems().addAll(Duration.values());
        durationCombo.setValue(Duration.UNTIL_RESTART);
    }

    private void setupWhatCombo() {
        operatorCombo.getItems().add(new Operator(Operand.USER_ID, "from user " + con.getUserName(), Type.SIMPLE));
        operatorCombo.getItems().add(new Operator(Operand.DEST_IP, "to " + con.getDstIp(), Type.SIMPLE));
        var destHost = new Operator(Operand.DEST_HOST, "to " + con.getDstHost(), Type.SIMPLE);
        operatorCombo.getItems().add(destHost); // TODO check if dst host is alright

        var mainDomain = DomainUtil.getMainDomain(con.getDstHost());
        if (!con.getDstHost().equals(mainDomain)) {
            // TODO check if IP Address
            this.mainDstDomain = mainDomain;
            operatorCombo.getItems().add(new Operator(Operand.DEST_MAIN_HOST, "to *." + mainDomain, Type.REGEXP));
        }

        operatorCombo.setConverter(new StringConverter<>() {

            @Override
            public String toString(Operator operator) {
                return operator.getText();
            }

            @Override
            public Operator fromString(String string) {
                return operatorCombo.getItems().stream().filter(operator ->
                        operator.getText().equals(string)).findFirst().orElse(null);
            }
        });

        operatorCombo.setValue(destHost);
    }

    public void setConnection(AFConnectionInfo con) {
        this.con = con;
        setupRequest();
    }

    public AFRule getRule() {
        return rule;
    }
}
