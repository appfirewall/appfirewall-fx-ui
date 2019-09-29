package io.appfirewall.fx.ui.statistics.columns;

import com.google.common.base.Preconditions;
import io.appfirewall.fx.ui.linux.AppInfo;

/**
 * A cell entry that can either hold a time to display or an app info
 */
public class AppInfoOrTime {
    private final String time;
    private final AppInfo appInfo;

    public AppInfoOrTime(AppInfo appInfo) {
        Preconditions.checkNotNull(appInfo);
        this.appInfo = appInfo;
        this.time = null;
    }

    public AppInfoOrTime(String time) {
        Preconditions.checkNotNull(time);
        this.time = time;
        this.appInfo = null;
    }

    public AppInfo getAppInfo() {
        return appInfo;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        if (time != null) {
            return time;
        }
        return appInfo.getAppName();
    }
}
