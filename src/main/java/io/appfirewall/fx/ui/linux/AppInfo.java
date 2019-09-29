package io.appfirewall.fx.ui.linux;

import com.google.common.base.Preconditions;

import java.util.Objects;

public class AppInfo {
    private final String appName;
    private final String appIcon;

    public AppInfo(String appName, String appIcon) {
        this.appName = Preconditions.checkNotNull(appName);
        this.appIcon = Preconditions.checkNotNull(appIcon);
    }

    public String getAppName() {
        return appName;
    }

    public String getAppIcon() {
        return appIcon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppInfo appInfo = (AppInfo) o;
        return appName.equals(appInfo.appName) &&
                appIcon.equals(appInfo.appIcon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appName, appIcon);
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "appName='" + appName + '\'' +
                ", appIcon='" + appIcon + '\'' +
                '}';
    }
}
