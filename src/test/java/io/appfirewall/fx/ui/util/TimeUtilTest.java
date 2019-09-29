package io.appfirewall.fx.ui.util;

import org.junit.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeUtilTest {

    @Test
    public void formatDuration() {
        assertThat(TimeUtil.formatDuration(Duration.ofSeconds(90))).isEqualTo("0:01:30");
        assertThat(TimeUtil.formatDuration(Duration.ofSeconds(3600))).isEqualTo("1:00:00");
    }
}