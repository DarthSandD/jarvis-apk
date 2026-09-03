package com.darrenai.jarvis.ai;

/**
 * Encrypted preferences storage for JARVIS AI provider settings.
 *
 * Uses AndroidX Security Crypto for API keys, falls back to plain
 * SharedPreferences if the keystore is unavailable on this device.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u0000  2\u00020\u0001:\u0001 B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u001e\u001a\u00020\u001fR\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R$\u0010\t\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\b8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR$\u0010\u000e\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\b8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u000f\u0010\u000b\"\u0004\b\u0010\u0010\rR$\u0010\u0011\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\b8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u0012\u0010\u000b\"\u0004\b\u0013\u0010\rR$\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\b8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u0015\u0010\u000b\"\u0004\b\u0016\u0010\rR\u000e\u0010\u0017\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R$\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u0007\u001a\u00020\u00188F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001d\u00a8\u0006!"}, d2 = {"Lcom/darrenai/jarvis/ai/PreferencesHelper;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "encryptedPrefs", "Landroid/content/SharedPreferences;", "value", "", "localEndpoint", "getLocalEndpoint", "()Ljava/lang/String;", "setLocalEndpoint", "(Ljava/lang/String;)V", "localModel", "getLocalModel", "setLocalModel", "openAiApiKey", "getOpenAiApiKey", "setOpenAiApiKey", "openAiModel", "getOpenAiModel", "setOpenAiModel", "plainPrefs", "Lcom/darrenai/jarvis/ai/AiProvider;", "selectedProvider", "getSelectedProvider", "()Lcom/darrenai/jarvis/ai/AiProvider;", "setSelectedProvider", "(Lcom/darrenai/jarvis/ai/AiProvider;)V", "migrateToEncrypted", "", "Companion", "app_debug"})
public final class PreferencesHelper {
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String ENCRYPTED_PREFS_NAME = "jarvis_secure_prefs";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String PLAIN_PREFS_NAME = "jarvis_settings";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String KEY_SELECTED_PROVIDER = "selected_provider";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String KEY_OPENAI_MODEL = "openai_model";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String KEY_LOCAL_ENDPOINT = "local_endpoint";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String KEY_LOCAL_MODEL = "local_model";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String KEY_OPENAI_API_KEY = "openai_api_key";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String DEFAULT_OPENAI_MODEL = "gpt-4o-mini";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String DEFAULT_LOCAL_ENDPOINT = "http://localhost:8081/v1";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String DEFAULT_LOCAL_MODEL = "llama-3.1-8b";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String DEFAULT_PROVIDER = "hermes";
    @org.jetbrains.annotations.Nullable
    private final android.content.SharedPreferences encryptedPrefs = null;
    @org.jetbrains.annotations.NotNull
    private final android.content.SharedPreferences plainPrefs = null;
    @org.jetbrains.annotations.NotNull
    public static final com.darrenai.jarvis.ai.PreferencesHelper.Companion Companion = null;
    
    public PreferencesHelper(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.darrenai.jarvis.ai.AiProvider getSelectedProvider() {
        return null;
    }
    
    public final void setSelectedProvider(@org.jetbrains.annotations.NotNull
    com.darrenai.jarvis.ai.AiProvider value) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getOpenAiModel() {
        return null;
    }
    
    public final void setOpenAiModel(@org.jetbrains.annotations.NotNull
    java.lang.String value) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getOpenAiApiKey() {
        return null;
    }
    
    public final void setOpenAiApiKey(@org.jetbrains.annotations.NotNull
    java.lang.String value) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getLocalEndpoint() {
        return null;
    }
    
    public final void setLocalEndpoint(@org.jetbrains.annotations.NotNull
    java.lang.String value) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getLocalModel() {
        return null;
    }
    
    public final void setLocalModel(@org.jetbrains.annotations.NotNull
    java.lang.String value) {
    }
    
    /**
     * Migration helper: move an API key stored in plain prefs to encrypted.
     */
    public final void migrateToEncrypted() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000b\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/darrenai/jarvis/ai/PreferencesHelper$Companion;", "", "()V", "DEFAULT_LOCAL_ENDPOINT", "", "DEFAULT_LOCAL_MODEL", "DEFAULT_OPENAI_MODEL", "DEFAULT_PROVIDER", "ENCRYPTED_PREFS_NAME", "KEY_LOCAL_ENDPOINT", "KEY_LOCAL_MODEL", "KEY_OPENAI_API_KEY", "KEY_OPENAI_MODEL", "KEY_SELECTED_PROVIDER", "PLAIN_PREFS_NAME", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}