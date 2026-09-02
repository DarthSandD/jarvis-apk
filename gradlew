#!/bin/bash
# Gradle wrapper script for JARVIS APK
# Generated for Android SDK builds

GRADLE_USER_HOME="${GRADLE_USER_HOME:-$HOME/.gradle}"

# Check for Android SDK
if [ -z "$ANDROID_HOME" ] && [ -z "$ANDROID_SDK_ROOT" ]; then
    echo "ERROR: ANDROID_HOME or ANDROID_SDK_ROOT not set." >&2
    echo "Set it to your Android SDK path, e.g.:" >&2
    echo "  export ANDROID_HOME=\"C:/Users/USER/AppData/Local/Android/Sdk\"" >&2
    exit 1
fi

# Use system gradle if available, otherwise download wrapper
if command -v gradle &>/dev/null; then
    exec gradle "$@"
else
    echo "ERROR: gradle not found. Install via:" >&2
    echo "  choco install gradle" >&2
    echo "  or download from https://gradle.org/install/" >&2
    exit 1
fi
