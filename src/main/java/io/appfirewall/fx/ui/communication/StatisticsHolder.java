package io.appfirewall.fx.ui.communication;

import io.appfirewall.protocol.AFStatistics;

public class StatisticsHolder {
    private static AFStatistics stats; // TODO improve

    public static void setStatistics(AFStatistics stats) {
        StatisticsHolder.stats = stats;
    }

    public static AFStatistics getStatistics() {
        return stats;
    }

    // TODO add listener stuff?
}
