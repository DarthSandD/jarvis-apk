package com.darrenai.jarvis.activity;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\u0018\u0000 \u00132\u00020\u0001:\u0001\u0013B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0002J\b\u0010\t\u001a\u00020\bH\u0002J\u0012\u0010\n\u001a\u00020\b2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0014J\b\u0010\r\u001a\u00020\u000eH\u0016J\b\u0010\u000f\u001a\u00020\bH\u0002J\b\u0010\u0010\u001a\u00020\bH\u0002J\b\u0010\u0011\u001a\u00020\bH\u0002J\b\u0010\u0012\u001a\u00020\bH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/darrenai/jarvis/activity/SettingsActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/darrenai/jarvis/databinding/ActivitySettingsBinding;", "prefs", "Landroid/content/SharedPreferences;", "bindListeners", "", "loadSettings", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onSupportNavigateUp", "", "resetSettings", "saveSettings", "setupToolbar", "updateTheme", "Companion", "app_debug"})
public final class SettingsActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.darrenai.jarvis.databinding.ActivitySettingsBinding binding;
    private android.content.SharedPreferences prefs;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String PREFS_NAME = "jarvis_settings";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_SERVER_IP = "server_ip";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_SERVER_PORT = "server_port";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_VOICE_MODE = "voice_mode";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_VOICE_LANGUAGE = "voice_language";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_VOICE_TIMEOUT = "voice_timeout_ms";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_SAMPLE_RATE = "sample_rate";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_AUTO_CONNECT = "auto_connect";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_THEME_MODE = "theme_mode";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_NOTIFICATION_ENABLED = "notification_enabled";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String DEFAULT_SERVER_IP = "10.212.104.140";
    private static final int DEFAULT_SERVER_PORT = 20128;
    @org.jetbrains.annotations.NotNull
    public static final com.darrenai.jarvis.activity.SettingsActivity.Companion Companion = null;
    
    public SettingsActivity() {
        super();
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupToolbar() {
    }
    
    private final void loadSettings() {
    }
    
    private final void bindListeners() {
    }
    
    private final void saveSettings() {
    }
    
    private final void resetSettings() {
    }
    
    private final void updateTheme() {
    }
    
    @java.lang.Override
    public boolean onSupportNavigateUp() {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u000b\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/darrenai/jarvis/activity/SettingsActivity$Companion;", "", "()V", "DEFAULT_SERVER_IP", "", "DEFAULT_SERVER_PORT", "", "KEY_AUTO_CONNECT", "KEY_NOTIFICATION_ENABLED", "KEY_SAMPLE_RATE", "KEY_SERVER_IP", "KEY_SERVER_PORT", "KEY_THEME_MODE", "KEY_VOICE_LANGUAGE", "KEY_VOICE_MODE", "KEY_VOICE_TIMEOUT", "PREFS_NAME", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}