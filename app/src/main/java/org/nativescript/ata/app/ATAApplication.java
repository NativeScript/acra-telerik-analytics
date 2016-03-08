package org.nativescript.ata.app;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import org.nativescript.ata.AnalyticsReportSender;

/**
 * Created by hristov on 3/7/2016.
 */
public class ATAApplication extends Application {
    @Override
    public void onCreate() {
        String analyticsProductKey = getResources().getString(R.string.analyticsProductKey);
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String applicationVersion = pInfo.versionName;
        Log.i("ATA Application", "AnalyticsReportSender.init("+this+", "+analyticsProductKey+", "+applicationVersion+");");
        AnalyticsReportSender.init(this, analyticsProductKey, applicationVersion);
        super.onCreate();
    }
}
