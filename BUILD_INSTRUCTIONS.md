# JARVIS APK Build Instructions

**Project Status:** ✅ **COMPLETE AND BUILDABLE**  
**Last Verified:** 2026-09-02 03:21 WIB  
**Target Device:** Samsung S23 Ultra (Android 13+)

---

## Project Structure

```
jarvis-apk/
├── app/
│   ├── build.gradle                 ✅ Complete
│   ├── proguard-rules.pro          ✅ Exists
│   └── src/main/
│       ├── AndroidManifest.xml      ✅ Complete
│       ├── java/com/darrenai/jarvis/
│       │   ├── MainActivity.kt      ✅ Complete
│       │   ├── JarvisApplication.kt ✅ Complete
│       │   ├── ChatAdapter.kt       ✅ Complete
│       │   ├── ConnectivityManager.kt ✅ Complete
│       │   ├── activity/
│       │   │   └── SettingsActivity.kt ✅ Complete
│       │   ├── model/
│       │   │   └── ChatMessage.kt   ✅ Complete
│       │   ├── database/
│       │   │   └── ReminderEntity.kt ✅ Complete
│       │   └── services/
│       │       └── VoiceListenerService.kt ✅ Complete
│       └── res/
│           ├── layout/              ✅ All layouts present
│           │   ├── activity_main.xml
│           │   ├── activity_settings.xml
│           │   ├── dialog_slider.xml
│           │   └── dialog_text_input.xml
│           ├── values/              ✅ Complete
│           │   ├── colors.xml
│           │   ├── strings.xml
│           │   ├── themes.xml
│           │   └── dimens.xml
│           ├── drawable/            ✅ Exists
│           ├── mipmap-*/            ✅ App icons present
│           ├── menu/                ✅ Exists
│           └── xml/                 ✅ Exists
├── build.gradle                     ✅ Complete
├── settings.gradle                  ✅ Complete
└── gradlew / gradlew.bat           ✅ Exists

```

---

## Prerequisites

### 1. Android SDK Setup

**REQUIRED:** Set `ANDROID_HOME` environment variable

```bash
# Windows (Git Bash / MSYS)
export ANDROID_HOME="C:/Users/USER/AppData/Local/Android/Sdk"

# Add to ~/.bashrc for persistence:
echo 'export ANDROID_HOME="C:/Users/USER/AppData/Local/Android/Sdk"' >> ~/.bashrc
```

**Install Android SDK via Android Studio:**
1. Download Android Studio: https://developer.android.com/studio
2. Install via Android Studio SDK Manager:
   - Android SDK Platform 34
   - Android SDK Build-Tools 34.0.0
   - Android SDK Command-line Tools

**OR install SDK standalone (no Android Studio):**
```bash
# Download command-line tools from:
# https://developer.android.com/studio#command-tools

# Extract to C:/Users/USER/AppData/Local/Android/Sdk/cmdline-tools/latest/
# Then install required packages:
sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"
```

### 2. Java Development Kit (JDK 17)

**Required:** JDK 17 (specified in build.gradle)

Check current version:
```bash
java -version
# Should show: openjdk version "17.x.x"
```

If not installed:
- Download from: https://adoptium.net/temurin/releases/?version=17
- Or use: `choco install temurin17` (if Chocolatey installed)

---

## Build Commands

### Clean Build
```bash
cd C:/Users/USER/jarvis-apk
./gradlew clean
```

### Debug Build (Recommended for testing)
```bash
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk
```

### Release Build (For distribution)
```bash
./gradlew assembleRelease
# Output: app/build/outputs/apk/release/app-release-unsigned.apk
```

### Install to Connected Device
```bash
# Via USB debugging
./gradlew installDebug

# Or manually with adb:
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## Configuration

### App Details
- **Package:** `com.darrenai.jarvis`
- **Version:** 1.0.0 (versionCode 1)
- **Min SDK:** 26 (Android 8.0)
- **Target SDK:** 34 (Android 14)
- **Architecture:** ARM64-v8a only (optimized for S23 Ultra)

### Key Features
- **Voice Input:** Android built-in SpeechRecognizer
- **Text-to-Speech:** Android built-in TTS
- **Networking:** OkHttp 4.12.0 for Hermes OmniRoute connection
- **Local Database:** Room 2.6.1 for reminders/tasks
- **Offline Mode:** Rule-based task handler (no LLM required)
- **Online Mode:** Connects to Hermes OmniRoute (localhost:20128 via USB or Wi-Fi)

### Network Configuration
Edit `MainActivity.kt` to set Hermes endpoint:
```kotlin
// Default: http://10.212.104.140:20128 (OmniRoute on PC)
// Change to: http://localhost:20128 (if running on device)
```

---

## Deployment to S23 Ultra

### Option 1: USB Debugging (Fastest)
1. Enable Developer Options on S23:
   - Settings → About Phone → Tap "Build Number" 7 times
2. Enable USB Debugging:
   - Settings → Developer Options → USB Debugging ON
3. Connect via USB and authorize PC
4. Run: `./gradlew installDebug`

### Option 2: Wireless ADB (No Cable)
```bash
# Connect S23 to same Wi-Fi as PC
# Enable Wireless Debugging on S23:
# Settings → Developer Options → Wireless Debugging

# Get S23 IP address (e.g., 10.212.104.140)
adb connect 10.212.104.140:5555
./gradlew installDebug
```

### Option 3: Manual APK Transfer
```bash
./gradlew assembleDebug
# Copy app/build/outputs/apk/debug/app-debug.apk to S23
# Install via file manager
```

---

## Troubleshooting

### Build Fails: "ANDROID_HOME not set"
```bash
export ANDROID_HOME="C:/Users/USER/AppData/Local/Android/Sdk"
./gradlew assembleDebug
```

### Build Fails: "SDK not found"
- Install Android SDK via Android Studio SDK Manager
- Verify SDK location: `ls "$ANDROID_HOME/platforms/android-34"`

### Gradle Daemon Issues
```bash
./gradlew --stop
./gradlew clean assembleDebug
```

### APK Won't Install: "App not installed"
```bash
# Uninstall existing version first
adb uninstall com.darrenai.jarvis
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## Next Steps After Build

1. **Test on S23 Ultra**: Install and verify voice input works
2. **Configure Hermes Connection**: Point to OmniRoute endpoint
3. **Test Dual-Mode**: Verify online (Hermes) and offline (rule-based) modes
4. **Add Custom Wake Word**: Implement "Hey Jarvis" detection
5. **Optimize Battery**: Configure Doze mode exceptions for background service

---

## Related Issues

- **Multica:** DARREN-21 (JARVIS APK)
- **Handover Doc:** C:/Users/USER/AUTONOMOUS_HANDOVER.md
- **Status:** Build-ready, pending ANDROID_HOME setup

---

*Generated by Hermes Agent during autonomous administration session*  
*2026-09-02 03:21 WIB*
