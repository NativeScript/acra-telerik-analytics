package org.nativescript.ata;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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

    public static void init(Application androidApplication, String analyticsProductKey) {
        try {
            Log.i("ATA", "Initializing ACRA...");
            final ACRAConfiguration config = new ACRAConfiguration(null);
            config.setCustomReportContent(REPORT_FIELDS);
            //config.setMailTo("nativescriptteam@gmail.com");
            ACRA.init(androidApplication, config, true);
            ReportSender reportSender = new AnalyticsReportSender(androidApplication, analyticsProductKey);
            ACRA.getErrorReporter().addReportSender(reportSender);
            Log.i("ATA", "ACRA initialized.");
        } catch (Exception e) {
            Log.e("ATA", e.getMessage());
            e.printStackTrace();
        }
    }

    public AnalyticsReportSender(Application androidApplication, String analyticsProductKey) {
        try {
            Log.i("ATA", "Creating ACRA AnalyticsReportSender...");
            int applicationVersionCode = AnalyticsReportSender.getApplicationVersionCode(androidApplication);

            // Telerik Analytics wants the version to be in the format major.minor[.build[.revision]], but we want to track our version code instead of version name
            // That is why we will send them a fake version name consisting of "applicationVersionCode.0"
            String applicationVersionName = applicationVersionCode + ".0";

            Log.i("ATA", "Creating Telerik AnalyticsMonitor with product key " + analyticsProductKey + " and application version code " + applicationVersionName + "...");
            IAnalyticsMonitorSettings settings = AnalyticsMonitorFactory.createSettings(analyticsProductKey, new Version(applicationVersionName));
            settings.setLoggingInterface(AnalyticsMonitorFactory.createTraceMonitor());
            settings.setUseSsl(true);
            this.monitor = AnalyticsMonitorFactory.createMonitor(androidApplication, settings);
            String installationId = Installation.id(androidApplication);
            this.monitor.setInstallationInfo(installationId);
            Log.i("ATA", "Telerik AnalyticsMonitor created with installationId " + installationId);
            Log.i("ATA", "ACRA AnalyticsReportSender created.");
        } catch (Exception e) {
            Log.e("ATA", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void send(Context context, CrashReportData report) throws ReportSenderException {
        try {
            Log.i("ATA", "Sending ACRA CrashReportData to Telerik Analytics...");

            if (this.monitor == null) {
                Log.e("ATA", "No AnalyticsMonitor found.");
                return;
            }

            if (!this.monitor.getStatus().getIsStarted()) {
                Log.i("ATA", "Starting Telerik AnalyticsMonitor...");
                this.monitor.start();
                Log.i("ATA", "Telerik AnalyticsMonitor started.");
            }

            String realStackTrace = report.get(ReportField.STACK_TRACE);

            // The stack trace hash will be sent to Telerik Analytics as the exception message so we
            // can group by this field to detect identical exceptions.
            String stackTraceHash = Integer.toString(realStackTrace.hashCode());
            Log.i("ATA", "Stack Trace hash is " + stackTraceHash);

            String exceptionType = realStackTrace.substring(0, realStackTrace.indexOf(':'));
            Log.i("ATA", "Detected exceptionType is " + exceptionType);

            // We will send the entire detailed ACRA report as the StackTrace field to Telerik Analytics.
            String pseudoStackTrace = report.toJSON().toString();

            this.monitor.trackExceptionRawMessage(exceptionType,
                    stackTraceHash,
                    pseudoStackTrace,
                    stackTraceHash);

            Log.i("ATA", "ACRA CrashReportData sent to Telerik Analytics.");

            if (this.monitor.getStatus().getIsStarted()) {
                Log.i("ATA", "Stopping Telerik AnalyticsMonitor...");
                this.monitor.stop();
                Log.i("ATA", "Telerik AnalyticsMonitor stopped.");
            }
        } catch (Exception e) {
            Log.e("ATA", e.getMessage());
            e.printStackTrace();
        }
    }

    private static int getApplicationVersionCode(Application androidApplication){
        try {
            PackageInfo pInfo = androidApplication.getPackageManager().getPackageInfo(androidApplication.getPackageName(), 0);
            int versionCode = pInfo.versionCode;
            return versionCode;
        } catch (Exception e) {
            Log.e("ATA", e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    private static final ReportField[] REPORT_FIELDS = {
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
            //ReportField.CUSTOM_DATA,
            ReportField.IS_SILENT,
            ReportField.STACK_TRACE,
            ReportField.INITIAL_CONFIGURATION,
            ReportField.CRASH_CONFIGURATION,
            ReportField.DISPLAY,
            ReportField.USER_COMMENT,
            ReportField.USER_EMAIL,
            ReportField.USER_APP_START_DATE,
            ReportField.USER_CRASH_DATE,
            //ReportField.DUMPSYS_MEMINFO,
            //LOGCAT,
            ReportField.INSTALLATION_ID,
            ReportField.DEVICE_FEATURES,
            ReportField.ENVIRONMENT,
            //ReportField.SHARED_PREFERENCES
    };
}
