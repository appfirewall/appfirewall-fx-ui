package io.appfirewall.fx.ui.statistics;

import com.google.common.flogger.FluentLogger;
import io.appfirewall.fx.ui.communication.StatisticsHolder;
import io.appfirewall.fx.ui.linux.IconLocator;
import io.appfirewall.fx.ui.linux.LinuxDesktopParser;
import io.appfirewall.fx.ui.linux.UserParser;
import io.appfirewall.fx.ui.statistics.columns.AppInfoOrTime;
import io.appfirewall.fx.ui.statistics.columns.EventOfExecutableRow;
import io.appfirewall.fx.ui.statistics.columns.EventsTableRow;
import io.appfirewall.fx.ui.statistics.columns.KeyCountRow;
import io.appfirewall.fx.ui.util.TimeUtil;
import io.appfirewall.protocol.AFConnectionInfo;
import io.appfirewall.protocol.AFStatistics;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

// TODO use Redux for State management?
public class StatisticsController implements Initializable {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    @FXML
    private Tab generalTab;
    @FXML
    private Label versionLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label uptimeLabel;
    @FXML
    private Label ruleCountLabel;
    @FXML
    private Label connectionCountLabel;
    @FXML
    private Label droppedConnectionCountLabel;

    @FXML
    private TreeTableView<EventOfExecutableRow> eventsByExecTable;

    @FXML
    private TreeTableColumn<EventOfExecutableRow, AppInfoOrTime> byExecTimeCol;
    @FXML
    private TreeTableColumn<EventOfExecutableRow, String> byExecActionCol;
    @FXML
    private TreeTableColumn<EventOfExecutableRow, String> byExecDestinationCol;
    @FXML
    private TreeTableColumn<EventOfExecutableRow, String> byExecRuleNameCol;


    @FXML
    private Tab eventsTab;
    private ObservableList<EventsTableRow> eventsData = FXCollections.observableArrayList();
    @FXML
    private TableView<EventsTableRow> eventsTable;
    @FXML
    private TableColumn<EventsTableRow, String> timeCol;
    @FXML
    private TableColumn<EventsTableRow, String> actionCol;
    @FXML
    private TableColumn<EventsTableRow, String> executableCol;
    @FXML
    private TableColumn<EventsTableRow, String> destinationCol;
    @FXML
    private TableColumn<EventsTableRow, String> ruleNameCol;

    @FXML
    private Tab hostsTab;
    private ObservableList<KeyCountRow> hostsData = FXCollections.observableArrayList();
    @FXML
    private TableView<KeyCountRow> hostsTable;
    @FXML
    private TableColumn<KeyCountRow, String> hostnameCol;
    @FXML
    private TableColumn<KeyCountRow, Long> hostConnectionCountCol;

    @FXML
    private Tab executablesTab;
    private ObservableList<KeyCountRow> executablesData = FXCollections.observableArrayList();
    @FXML
    private TableView<KeyCountRow> executablesTable;
    @FXML
    private TableColumn<KeyCountRow, String> executablesExecutableCol;
    @FXML
    private TableColumn<KeyCountRow, Long> executablesConnectionCountCol;


    @FXML
    private Tab addressesTab;
    private ObservableList<KeyCountRow> addressesData = FXCollections.observableArrayList();
    @FXML
    private TableView<KeyCountRow> addressesTable;
    @FXML
    private TableColumn<KeyCountRow, String> addressCol;
    @FXML
    private TableColumn<KeyCountRow, Long> addressConnectionCountCol;

    @FXML
    private Tab usersTab;
    private ObservableList<KeyCountRow> usersData = FXCollections.observableArrayList();
    @FXML
    private TableView<KeyCountRow> usersTable;
    @FXML
    private TableColumn<KeyCountRow, String> userCol;
    @FXML
    private TableColumn<KeyCountRow, Long> userConnectionCountCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeGeneralTab();
        initializeEventsTable();
        initializeColumnPair(hostnameCol, hostConnectionCountCol);
        initializeColumnPair(executablesExecutableCol, executablesConnectionCountCol);
        initializeColumnPair(addressCol, addressConnectionCountCol);
        initializeColumnPair(userCol, userConnectionCountCol);

        addTabChangeListener(generalTab, () -> updateGeneralTab());
        addTabChangeListener(eventsTab, () -> updateEventsTable());
        addTabChangeListener(hostsTab, () -> updatePairTable(StatisticsHolder.getStatistics().getByHostMap(), hostsData, hostsTable, hostConnectionCountCol));
        addTabChangeListener(executablesTab, () -> updatePairTable(StatisticsHolder.getStatistics().getByExecutableMap(), executablesData, executablesTable, executablesConnectionCountCol));
        addTabChangeListener(addressesTab, () -> updatePairTable(StatisticsHolder.getStatistics().getByAddressMap(), addressesData, addressesTable, addressConnectionCountCol));
        addTabChangeListener(usersTab, () -> setupUserTable(StatisticsHolder.getStatistics().getByUidMap(), usersData, usersTable, userConnectionCountCol));

        updateGeneralTab();
    }

    private static void addTabChangeListener(Tab tab, Runnable updateFunction) {
        tab.setOnSelectionChanged((event) -> {
            if (StatisticsHolder.getStatistics() == null) {
                return;
            }
            if (tab.isSelected()) {
                updateFunction.run();
            }
        });
    }

    private void initializeGeneralTab() {
        final TreeItem<EventOfExecutableRow> rootNode =
                new TreeItem<>(new EventOfExecutableRow(new AppInfoOrTime(""), "", "", ""));
        eventsByExecTable.setRoot(rootNode);
        eventsByExecTable.setShowRoot(false);
        rootNode.setExpanded(true);

        // byExecTimeCol.setCellValueFactory(c -> c.getValue().getValue().timeProperty());

        //byExecTimeCol.setCellValueFactory(c -> c.getValue().getValue().getAppInfoOrTime());

        byExecTimeCol.setCellValueFactory(c -> c.getValue().getValue().appInfoOrTimeProperty());
        byExecActionCol.setCellValueFactory(c -> c.getValue().getValue().actionProperty());
        byExecDestinationCol.setCellValueFactory(c -> c.getValue().getValue().destinationProperty());
        byExecRuleNameCol.setCellValueFactory(c -> c.getValue().getValue().ruleNameProperty());


        byExecTimeCol.setCellFactory(column -> new TreeTableCell<>() {
            @Override
            public void updateItem(AppInfoOrTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                    return;
                }
                var appInfo = item.getAppInfo();
                if (appInfo != null) {
                    setText(appInfo.getAppName());
                    this.setStyle("-fx-font-weight: bold");

                    IconLocator.getIconForName(appInfo.getAppIcon()).ifPresentOrElse(
                            iconPath -> {
                                // TODO use slightly larger icons (24x24) and reposition arrow
                                var image = new Image(iconPath.toUri().toString(), 16, 16, true, true);
                                setGraphic(new ImageView(image));
                            },
                            () -> setGraphic(null)
                    );
                } else {
                    this.setStyle(null);
                    setText(item.getTime());
                    setGraphic(null);
                }
            }
        });

        byExecDestinationCol.setCellFactory(column -> new TreeTableCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!isEmpty()) {
                    if (item.startsWith("/")) {
                        this.setTextFill(Color.DARKGRAY);
                    } else {
                        this.setTextFill(Color.BLACK);
                    }
                }
                setText(item);
            }
        });
    }

    private void initializeEventsTable() {
        timeCol.setCellValueFactory(c -> c.getValue().timeProperty());
        actionCol.setCellValueFactory(c -> c.getValue().actionProperty());
        executableCol.setCellValueFactory(c -> c.getValue().executableProperty());
        destinationCol.setCellValueFactory(c -> c.getValue().destinationProperty());
        ruleNameCol.setCellValueFactory(c -> c.getValue().ruleNameProperty());
    }

    private void initializeColumnPair(TableColumn<KeyCountRow, String> nameCol, TableColumn<KeyCountRow, Long> countCol) {
        nameCol.setCellValueFactory(c -> c.getValue().keyProperty());
        countCol.setCellValueFactory(c -> c.getValue().connectionCountProperty().asObject());
        countCol.setSortType(TableColumn.SortType.DESCENDING);
    }

    private void updateGeneralTab() {
        AFStatistics stats = StatisticsHolder.getStatistics();
        if (stats == null) {
            statusLabel.setText("no data");
            return;
        }

        versionLabel.setText(stats.getAppfirewallVersion());

        statusLabel.setText("data received"); // TODO check connected, not connected

        var uptime = Duration.ofSeconds(stats.getUptime());
        uptimeLabel.setText(TimeUtil.formatDuration(uptime));
        ruleCountLabel.setText(stats.getRules() + "");
        connectionCountLabel.setText(stats.getConnections() + "");
        droppedConnectionCountLabel.setText(stats.getDropped() + "");

        updateEventsByExecutableTable();
    }

    private void updateEventsByExecutableTable() {
        eventsByExecTable.getRoot().getChildren().clear();
        AFStatistics stats = StatisticsHolder.getStatistics();

        var executables = new HashMap<String, TreeItem<EventOfExecutableRow>>();

        for (var event : stats.getEventsList()) {
            var execPath = event.getConnection().getProcessPath();
            var entry  = executables.get(execPath);
            if (entry == null) {
                entry = createExecutableHeaderEntry(execPath);
                executables.put(execPath, entry);
            }
            var eventOfExec = new EventOfExecutableRow(new AppInfoOrTime(event.getTime()),
                    event.getRule().getAction(),
                    getDestination(event.getConnection()),
                    event.getRule().getName());
            entry.getChildren().add(new TreeItem<>(eventOfExec, new ImageView()));
        }

        eventsByExecTable.getRoot().getChildren().addAll(executables.values());
        eventsByExecTable.refresh();
    }

    private static TreeItem<EventOfExecutableRow> createExecutableHeaderEntry(String execPath) {
        var appInfo = LinuxDesktopParser.getInfoByPath(execPath);

        var column = new EventOfExecutableRow(new AppInfoOrTime(appInfo), "", execPath, "");
        return new TreeItem<>(column);
    }


    private void updateEventsTable() {
        AFStatistics stats = StatisticsHolder.getStatistics();
        eventsData.clear();
        var eventList = stats.getEventsList()
                .stream()
                .map(e -> new EventsTableRow(
                        e.getTime(),
                        e.getRule().getAction(),
                        e.getConnection().getProcessPath(),
                        getDestination(e.getConnection()),
                        e.getRule().getName()))
                .collect(Collectors.toList());

        eventsData.addAll(eventList);

        eventsTable.setItems(eventsData);
        eventsTable.refresh();
    }

    private String getDestination(AFConnectionInfo con) {
        if (con.getDstHost().isEmpty()) {
            return con.getDstIp() + ":" + con.getDstIp();
        }
        return con.getDstHost();
    }

    private static void updatePairTable(Map<String, Long> data, ObservableList<KeyCountRow> newData, TableView<KeyCountRow> table, TableColumn<KeyCountRow, Long> sortBy) {
        try {
            newData.clear();
            var list = data.entrySet()
                    .stream()
                    .map(e -> new KeyCountRow(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());

            newData.addAll(list);

            table.setItems(newData);
            table.refresh();
            table.getSortOrder().add(sortBy);
        } catch (Exception ex) {
            logger.atWarning().withCause(ex).log("Could not fill table");
        }
    }

    private static void setupUserTable(Map<String, Long> data, ObservableList<KeyCountRow> newData, TableView<KeyCountRow> table, TableColumn<KeyCountRow, Long> sortBy) {
        try {
            newData.clear();
            var list = data.entrySet()
                    .stream()
                    .map(e -> new KeyCountRow(UserParser.getFormattedUserForUid(e.getKey()), e.getValue()))
                    .collect(Collectors.toList());

            newData.addAll(list);

            table.setItems(newData);
            table.refresh();
            table.getSortOrder().add(sortBy);
        } catch (Exception ex) {
            logger.atWarning().withCause(ex).log("Could not fill user table");
        }
    }
}
