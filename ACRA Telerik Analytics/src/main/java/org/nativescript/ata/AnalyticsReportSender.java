package org.nativescript.ata;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import org.acra.ACRA;
import org.acra.ACRAConfiguration;
import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;
import org.acra.util.Installation;

import eqatec.analytics.monitor.AnalyticsMonitorFactory;
import eqatec.analytics.monitor.IAnalyticsMonitor;
import eqatec.analytics.monitor.IAnalyticsMonitorSettings;
import eqatec.analytics.monitor.Version;

/**
 * Created by hristov on 3/7/2016.
 */
public class AnalyticsReportSender implements ReportSender {
    private IAnalyticsMonitor monitor;

    public static void init(Application androidApplication, String analyticsProductKey, String appVersion) {
        try {
            Log.i("ATA", "Initializing ACRA...");
            final ACRAConfiguration config = new ACRAConfiguration(null);
            config.setCustomReportContent(REPORT_FIELDS);
            //config.setMailTo("nativescriptteam@gmail.com");
            ACRA.init(androidApplication, config, true);
            ReportSender reportSender = new AnalyticsReportSender(androidApplication, analyticsProductKey, appVersion);
            ACRA.getErrorReporter().addReportSender(reportSender);
            Log.i("ATA", "ACRA initialized.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AnalyticsReportSender(Application androidApplication, String analyticsProductKey, String appVersion) {
        try {
            Log.i("ATA", "Creating ACRA AnalyticsReportSender...");
            Log.i("ATA", "Creating Telerik AnalyticsMonitor with product key " + analyticsProductKey + " and application version " + appVersion + "...");
            IAnalyticsMonitorSettings settings = AnalyticsMonitorFactory.createSettings(analyticsProductKey, new Version(appVersion));
            settings.setLoggingInterface(AnalyticsMonitorFactory.createTraceMonitor());
            settings.setUseSsl(true);
            this.monitor = AnalyticsMonitorFactory.createMonitor(androidApplication, settings);
            String installationId = Installation.id(androidApplication);
            this.monitor.setInstallationInfo(installationId);
            Log.i("ATA", "Telerik AnalyticsMonitor created with installationId " + installationId);
            Log.i("ATA", "ACRA AnalyticsReportSender created.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(Context context, CrashReportData report) throws ReportSenderException {
        try {
            Log.i("ATA", "Sending ACRA CrashReportData to Telerik Analytics...");

            if (!this.monitor.getStatus().getIsStarted()) {
                Log.i("ATA", "Starting Telerik AnalyticsMonitor...");
                this.monitor.start();
                Log.i("ATA", "Telerik AnalyticsMonitor started.");
            }

            String realStackTrace = report.get(ReportField.STACK_TRACE);
            String exceptionType = realStackTrace.substring(0, realStackTrace.indexOf(':'));
            Log.i("ATA", "Detected exceptionType is " + exceptionType);
            // We will send the entire detailed ACRA report as the StackTrace field to Telerik Analytics.
            String pseudoStackTrace = report.toJSON().toString();

            this.monitor.trackExceptionRawMessage(exceptionType,
                    "ACRA detected an application crash. Full crash details are contained in the pseudo stack trace as JSON.",
                    pseudoStackTrace,
                    "ACRA detected an application crash. Full crash details are contained in the pseudo stack trace as JSON.");

            Log.i("ATA", "ACRA CrashReportData sent to Telerik Analytics.");

            if (this.monitor.getStatus().getIsStarted()) {
                Log.i("ATA", "Stopping Telerik AnalyticsMonitor...");
                this.monitor.stop();
                Log.i("ATA", "Telerik AnalyticsMonitor stopped.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final ReportField[] REPORT_FIELDS = {
            ReportField.REPORT_ID,
            ReportField.APP_VERSION_CODE,
            ReportField.APP_VERSION_NAME,
            ReportField.PACKAGE_NAME,
            ReportField.FILE_PATH,
            ReportField.PHONE_MODEL,
            ReportField.BRAND,
            ReportField.PRODUCT,
            ReportField.ANDROID_VERSION,
            ReportField.BUILD,
            ReportField.TOTAL_MEM_SIZE,
            ReportField.AVAILABLE_MEM_SIZE,
            ReportField.BUILD_CONFIG,
            ReportField.CUSTOM_DATA,
            ReportField.IS_SILENT,
            ReportField.STACK_TRACE,
            ReportField.INITIAL_CONFIGURATION,
            ReportField.CRASH_CONFIGURATION,
            ReportField.DISPLAY,
            ReportField.USER_COMMENT,
            ReportField.USER_EMAIL,
            ReportField.USER_APP_START_DATE,
            ReportField.USER_CRASH_DATE,
            ReportField.DUMPSYS_MEMINFO,
            //LOGCAT,
            ReportField.INSTALLATION_ID,
            ReportField.DEVICE_FEATURES,
            ReportField.ENVIRONMENT,
            ReportField.SHARED_PREFERENCES
    };
}
