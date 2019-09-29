package io.appfirewall.fx.ui.prompt;

public enum Type {
    SIMPLE("simple"),
    REGEXP("regexp");

    private final String value;

    Type(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
