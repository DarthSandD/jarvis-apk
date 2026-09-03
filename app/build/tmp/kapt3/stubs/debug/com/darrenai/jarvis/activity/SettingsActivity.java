package com.darrenai.jarvis.activity;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u0000 $2\u00020\u0001:\u0001$B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\t\u001a\u00020\nH\u0002J\b\u0010\u000b\u001a\u00020\nH\u0002J\b\u0010\f\u001a\u00020\nH\u0002J\u0012\u0010\r\u001a\u00020\n2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0014J\b\u0010\u0010\u001a\u00020\u0011H\u0016J\b\u0010\u0012\u001a\u00020\nH\u0002J\b\u0010\u0013\u001a\u00020\nH\u0002J\b\u0010\u0014\u001a\u00020\nH\u0002J\b\u0010\u0015\u001a\u00020\nH\u0002J\b\u0010\u0016\u001a\u00020\nH\u0002J\b\u0010\u0017\u001a\u00020\nH\u0002J\b\u0010\u0018\u001a\u00020\nH\u0002J\b\u0010\u0019\u001a\u00020\nH\u0002J1\u0010\u001a\u001a\u00020\n2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 2\n\b\u0002\u0010!\u001a\u0004\u0018\u00010\"H\u0002\u00a2\u0006\u0002\u0010#R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"Lcom/darrenai/jarvis/activity/SettingsActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "aiService", "Lcom/darrenai/jarvis/ai/AiService;", "binding", "Lcom/darrenai/jarvis/databinding/ActivitySettingsBinding;", "prefs", "Lcom/darrenai/jarvis/ai/PreferencesHelper;", "autoDetectLocal", "", "bindListeners", "loadSettings", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onSupportNavigateUp", "", "resetSettings", "runHealthChecks", "saveSettings", "setupAiProviderSection", "setupToolbar", "testHermesConnection", "testLocalConnection", "testOpenAiConnection", "updateStatusDot", "dot", "Landroid/view/View;", "label", "Landroid/widget/TextView;", "text", "", "colorRes", "", "(Landroid/view/View;Landroid/widget/TextView;Ljava/lang/String;Ljava/lang/Integer;)V", "Companion", "app_debug"})
public final class SettingsActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.darrenai.jarvis.databinding.ActivitySettingsBinding binding;
    private com.darrenai.jarvis.ai.PreferencesHelper prefs;
    private com.darrenai.jarvis.ai.AiService aiService;
    @org.jetbrains.annotations.NotNull
    private static final java.util.List<java.lang.String> OPENAI_MODELS = null;
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
    
    private final void setupAiProviderSection() {
    }
    
    private final void loadSettings() {
    }
    
    private final void bindListeners() {
    }
    
    private final void saveSettings() {
    }
    
    private final void resetSettings() {
    }
    
    private final void runHealthChecks() {
    }
    
    private final void testOpenAiConnection() {
    }
    
    private final void testLocalConnection() {
    }
    
    private final void testHermesConnection() {
    }
    
    private final void autoDetectLocal() {
    }
    
    private final void updateStatusDot(android.view.View dot, android.widget.TextView label, java.lang.String text, java.lang.Integer colorRes) {
    }
    
    @java.lang.Override
    public boolean onSupportNavigateUp() {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/darrenai/jarvis/activity/SettingsActivity$Companion;", "", "()V", "OPENAI_MODELS", "", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}