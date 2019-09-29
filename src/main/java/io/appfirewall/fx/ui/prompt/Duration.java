package io.appfirewall.fx.ui.prompt;

public enum Duration {
    ONCE("once"),
    UNTIL_RESTART("until restart"),
    ALWAYS("always");

    private final String title;

    Duration(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return this.title;
    }

    public String getTitle() {
        return title;
    }
}
