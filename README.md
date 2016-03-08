# ACRA Telerik Analytics
Contains the source code of the `org.nativescript.ata` library used by the [nativescript-marketplace-demo](https://github.com/NativeScript/nativescript-marketplace-demo) app for Android.

## How to Build
```
cd "ACRA Telerik Analytics"
../gradlew packFramework
```

This generates .tgz and .aar files in the dist folder.

If you need to make changes to the source code:
 1. Make your changes.
 2. Increment the version in package.json
 3. Go to the dist folder and `npm publish` the new tgz file.
 4. Update the dependency in the nativescript-marketplace-demo package.json to the new version.

## How to use from a NativeScript application for android

