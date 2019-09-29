package io.appfirewall.fx.ui.prompt;

public enum Action {
    ALLOW("allow"),
    DENY("deny");

    private final String value;

    Action(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
