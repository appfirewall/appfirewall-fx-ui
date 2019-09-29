package io.appfirewall.fx.ui.linux;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

// TODO delete
public class UserParserTest {
    @Test
    public void parseFileContent() {
        UserParser.parseFileContent("root:x:0:0:root:/root:/bin/bash\n" +
                "daemon:x:1:1:daemon:/usr/sbin:/usr/sbin/nologin\n" +
                "bin:x:2:2:bin:/bin:/usr/sbin/nologin\n" +
                "sys:x:3:7:sys:/dev:/usr/sbin/nologin\n");

        assertThat(UserParser.getUsernameForUid(0).get()).isEqualTo("root");
        assertThat(UserParser.getUsernameForUid(1).get()).isEqualTo("daemon");
        assertThat(UserParser.getUsernameForUid(2).get()).isEqualTo("bin");
        assertThat(UserParser.getUsernameForUid(3).get()).isEqualTo("sys");
        assertThat(UserParser.getUsernameForUid(7)).isEmpty();
    }
}
