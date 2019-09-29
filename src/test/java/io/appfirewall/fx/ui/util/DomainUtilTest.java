package io.appfirewall.fx.ui.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DomainUtilTest {

    @Test
    public void getMainDomain() {
        assertThat(DomainUtil.getMainDomain("github.com")).isEqualTo("github.com");
        assertThat(DomainUtil.getMainDomain("subdomain.github.com")).isEqualTo("github.com");
        assertThat(DomainUtil.getMainDomain("sub.subdomain.github.com")).isEqualTo("github.com");
        assertThat(DomainUtil.getMainDomain("bbc.co.uk")).isEqualTo("bbc.co.uk");
        assertThat(DomainUtil.getMainDomain("www.kangaroo.com.au")).isEqualTo("kangaroo.com.au");
        assertThat(DomainUtil.getMainDomain("hostname")).isEqualTo("hostname");
        assertThat(DomainUtil.getMainDomain("not-a-combined-tld.zh.ch")).isEqualTo("zh.ch");
    }

    @Test
    public void createDaemonRegex() {
        assertThat(DomainUtil.createDaemonRegex("snapcraft.io")).isEqualTo(".*\\.snapcraft\\.io");
    }
}