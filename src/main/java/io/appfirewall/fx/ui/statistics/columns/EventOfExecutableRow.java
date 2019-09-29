package io.appfirewall.fx.ui.statistics.columns;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class EventOfExecutableRow {
    private final SimpleObjectProperty<AppInfoOrTime> appInfoOrTime;
    private final SimpleStringProperty action;
    private final SimpleStringProperty destination;
    private final SimpleStringProperty ruleName;

    public EventOfExecutableRow(AppInfoOrTime appInfoOrTime, String action, String destination, String ruleName) {
        this.appInfoOrTime = new SimpleObjectProperty<>(appInfoOrTime);
        this.action = new SimpleStringProperty(action);
        this.destination = new SimpleStringProperty(destination);
        this.ruleName = new SimpleStringProperty(ruleName);
    }

    public AppInfoOrTime getAppInfoOrTime() {
        return appInfoOrTime.get();
    }

    public SimpleObjectProperty<AppInfoOrTime> appInfoOrTimeProperty() {
        return appInfoOrTime;
    }

    public String getAction() {
        return action.get();
    }

    public SimpleStringProperty actionProperty() {
        return action;
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
