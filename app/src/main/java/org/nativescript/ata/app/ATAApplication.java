package org.nativescript.ata.app;

import android.app.Application;

import org.nativescript.ata.AnalyticsReportSender;

/**
 * Created by hristov on 3/7/2016.
 */
public class ATAApplication extends Application {
    @Override
    public void onCreate() {
        AnalyticsReportSender.init(this, getResources().getString(R.string.analyticsProductKey));
        super.onCreate();
    }
}
