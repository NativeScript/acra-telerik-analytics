package org.nativescript.ata.app;

import android.app.Application;
import android.util.Log;

import org.nativescript.ata.AnalyticsReportSender;

/**
 * Created by hristov on 3/7/2016.
 */
public class ATAApplication extends Application {
    @Override
    public void onCreate() {
        String analyticsProductKey = getResources().getString(R.string.analyticsProductKey);
        Log.i("ATA Application", "AnalyticsReportSender.init("+this+", "+analyticsProductKey+");");
        AnalyticsReportSender.init(this, analyticsProductKey);
        super.onCreate();
    }
}
