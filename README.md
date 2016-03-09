# ACRA Telerik Analytics
Contains the source code of the `org.nativescript.ata` library used by the [nativescript-marketplace-demo](https://github.com/NativeScript/nativescript-marketplace-demo) app for Android.

## Build
```
cd "ACRA Telerik Analytics"
../gradlew packFramework
```

This generates .tgz and .aar files in the dist folder.

## Update
If you need to make changes to the source code:
 1. Make the source code changes.
 2. Increment the version in package.json
 3. Go to the `"ACRA Telerik Analytics"` folder.
 4. Build with `../gradlew packFramework`
 5. Go back to the repository root folder and then go to the `dist` folder
 3. `npm publish` the newly generated tgz file.

## Install
`npm install acra-telerik-analytics`

## How to use from a NativeScript application for Android
```
var application = require("application");

application.on(application.launchEvent, function(args) {
    if (application.android) {
        var analyticsProductKey = "The product key located in the auto-generated code-snippet when you create a new Analytics app for Android in Telerik Platform";
        org.nativescript.ata.AnalyticsReportSender.init(application.android.nativeApp, analyticsProductKey);
    }
});
```
## How to use from a native Android application
Add node_modules/acra-telerik-analytics/platforms/android/ACRA Telerik Analytics-release.aar as a dependency in your Android application project.
```
package org.nativescript.ata.app;

import android.app.Application;
import android.util.Log;

import org.nativescript.ata.AnalyticsReportSender;

public class ATAApplication extends Application {
    @Override
    public void onCreate() {
        String analyticsProductKey = "The product key located in the auto-generated code-snippet when you create a new Analytics app for Android in Telerik Platform";
        AnalyticsReportSender.init(this, analyticsProductKey);
        super.onCreate();
    }
}
```