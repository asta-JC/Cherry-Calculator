# Screenshot capture

The three Light-mode gallery images use the actual production composables with temporary demonstration state on a physical Samsung Galaxy S22 Ultra. The screenshot test does not change user history or saved appearance and captures app content without personal system chrome.

Build and install the app and test APK on one explicit physical device:

```bash
./gradlew :app:assembleDebug :app:assembleDebugAndroidTest
adb -s YOUR_DEVICE_SERIAL install -r app/build/outputs/apk/debug/app-debug.apk
adb -s YOUR_DEVICE_SERIAL install -r app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk
adb -s YOUR_DEVICE_SERIAL shell am instrument -w -e class com.asta.calculatorapp.ReadmeScreenshotsTest com.asta.calculatorapp.test/androidx.test.runner.AndroidJUnitRunner
adb -s YOUR_DEVICE_SERIAL pull /sdcard/Android/data/com.asta.calculatorapp/files/readme-screenshots/. docs/screenshots/
```

The gallery contains exactly `calculator-light.png`, `scientific-light.png`, and `history-light.png`. Keep all three at their captured dimensions and inspect them before publishing.
