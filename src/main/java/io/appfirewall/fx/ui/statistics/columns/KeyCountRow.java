package io.appfirewall.fx.ui.statistics.columns;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class KeyCountRow {
    private final SimpleStringProperty key;
    private final SimpleLongProperty connectionCount;

    public KeyCountRow(String hostname, long connectionCount) {
        this.key = new SimpleStringProperty(hostname);
        this.connectionCount = new SimpleLongProperty(connectionCount);
    }

    public String getKey() {
        return key.get();
    }

    public SimpleStringProperty keyProperty() {
        return key;
    }

    public long getConnectionCount() {
        return connectionCount.get();
    }

    public SimpleLongProperty connectionCountProperty() {
        return connectionCount;
    }
}
