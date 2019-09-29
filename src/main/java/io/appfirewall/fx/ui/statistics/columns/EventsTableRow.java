package io.appfirewall.fx.ui.statistics.columns;

import javafx.beans.property.SimpleStringProperty;

public class EventsTableRow {
    private final SimpleStringProperty time;
    private final SimpleStringProperty action;
    private final SimpleStringProperty executable;
    private final SimpleStringProperty destination;
    private final SimpleStringProperty ruleName;

    public EventsTableRow(String time, String action, String executable, String destination, String ruleName) {
        this.time = new SimpleStringProperty(time);
        this.action = new SimpleStringProperty(action);
        this.executable = new SimpleStringProperty(executable);
        this.destination = new SimpleStringProperty(destination);
        this.ruleName = new SimpleStringProperty(ruleName);
    }

    public String getTime() {
        return time.get();
    }

    public SimpleStringProperty timeProperty() {
        return time;
    }

    public String getAction() {
        return action.get();
    }

    public SimpleStringProperty actionProperty() {
        return action;
    }

    public String getExecutable() {
        return executable.get();
    }

    public SimpleStringProperty executableProperty() {
        return executable;
    }

    public String getDestination() {
        return destination.get();
    }

    public SimpleStringProperty destinationProperty() {
        return destination;
    }

    public String getRuleName() {
        return ruleName.get();
    }

    public SimpleStringProperty ruleNameProperty() {
        return ruleName;
    }
}
