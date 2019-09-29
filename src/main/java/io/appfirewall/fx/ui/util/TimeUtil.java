package io.appfirewall.fx.ui.util;

import java.time.Duration;

public class TimeUtil {

    public static String formatDuration(Duration duration) {
        return String.format("%d:%02d:%02d",
                duration.toHoursPart(),
                duration.toMinutesPart(),
                duration.toSecondsPart());
    }
}
