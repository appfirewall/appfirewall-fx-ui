package io.appfirewall.fx.ui.prompt;

public enum Operand {
    USER_ID("user.id"),
    DEST_IP("dest.ip"),
    DEST_HOST("dest.host"),
    DEST_MAIN_HOST("dest.host");


    private final String value;

    Operand(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
