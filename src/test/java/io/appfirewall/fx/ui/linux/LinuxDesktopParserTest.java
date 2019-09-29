package io.appfirewall.fx.ui.linux;

import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

public class LinuxDesktopParserTest {

    @Test
    public void cleanExec() {
        assertThat(LinuxDesktopParser.cleanExec("gnome-software %U")).isEqualTo("gnome-software");
        assertThat(LinuxDesktopParser.cleanExec("env TEST=1 gedit --new-window")).isEqualTo("gedit");
        assertThat(LinuxDesktopParser.cleanExec("/usr/share/code/code --new-window %F")).isEqualTo("/usr/share/code/code");
    }
}