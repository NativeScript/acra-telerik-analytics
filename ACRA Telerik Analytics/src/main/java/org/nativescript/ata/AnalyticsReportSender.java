package org.nativescript.ata;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import org.acra.ACRA;
import org.acra.ACRAConfiguration;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;
import org.acra.util.JSONReportBuilder;

import java.net.URISyntaxException;

import eqatec.analytics.monitor.AnalyticsMonitorFactory;
import eqatec.analytics.monitor.IAnalyticsMonitor;
import eqatec.analytics.monitor.Version;

/**
 * Created by hristov on 3/7/2016.
 */
public class AnalyticsReportSender implements ReportSender {
    private String analyticsProductKey;
    private IAnalyticsMonitor monitor;

    public static void init(Application app, String analyticsProductKey) {
        try {
            Log.i("ATA", "Initializing ACRA...");
            final ACRAConfiguration config = new ACRAConfiguration(null);
            //config.setMailTo("rossen.hristov@live.com");
            ACRA.init(app, config, true);
            ReportSender reportSender = new AnalyticsReportSender(app, analyticsProductKey);
            ACRA.getErrorReporter().addReportSender(reportSender);
            Log.i("ATA", "ACRA initialized.");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public AnalyticsReportSender(Application app, String analyticsProductKey){
        try {
            Log.i("ATA", "Creating AnalyticsReportSender...");
            this.analyticsProductKey = analyticsProductKey;
            Version version = new Version("3.2.61");
            Log.i("ATA", "Creating AnalyticsMonitor with product key " + this.analyticsProductKey + "...");
            this.monitor = AnalyticsMonitorFactory.createMonitor(app, analyticsProductKey, version);
            Log.i("ATA", "AnalyticsMonitor created.");
            Log.i("ATA", "Starting AnalyticsMonitor...");
            this.monitor.start();
            Log.i("ATA", "AnalyticsMonitor started.");
            Log.i("ATA", "AnalyticsReportSender created.");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(Context context, CrashReportData report) throws ReportSenderException {
        try {
            Log.i("ATA", "Sending ACRA CrashReportData to Telerik Analytics...");
            String reportAsString = report.toJSON().toString();
            this.monitor.trackExceptionRawMessage("ACRACrashReportData", "", "", reportAsString);
            Log.i("ATA", "ACRA CrashReportData sent.");
        } catch (JSONReportBuilder.JSONReportException e) {
            e.printStackTrace();
        }
    }
}
