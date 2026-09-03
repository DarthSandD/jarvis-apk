package com.darrenai.jarvis;

/**
 * MainActivity — JARVIS chat interface.
 *
 * Modern Android: edge-to-edge layout, Material 3, fluid animations,
 * multi-provider AI, voice I/O.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0011\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0014\u001a\u00020\u0015H\u0002J\u0012\u0010\u0016\u001a\u00020\u00152\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0014J\b\u0010\u0019\u001a\u00020\u0015H\u0014J\b\u0010\u001a\u001a\u00020\u0015H\u0014J\b\u0010\u001b\u001a\u00020\u0015H\u0014J\u0010\u0010\u001c\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u001eH\u0002J\b\u0010\u001f\u001a\u00020\u0015H\u0002J\b\u0010 \u001a\u00020\u0015H\u0002J\b\u0010!\u001a\u00020\u0015H\u0002J\b\u0010\"\u001a\u00020\u0015H\u0002J\b\u0010#\u001a\u00020\u0015H\u0002J\b\u0010$\u001a\u00020\u0015H\u0002J\b\u0010%\u001a\u00020\u0015H\u0002J\b\u0010&\u001a\u00020\u0015H\u0002J\b\u0010\'\u001a\u00020\u0015H\u0002J\u0010\u0010(\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u001eH\u0002J\u0010\u0010)\u001a\u00020\u00152\u0006\u0010*\u001a\u00020\nH\u0002J\b\u0010+\u001a\u00020\u0015H\u0002J\b\u0010,\u001a\u00020\u0015H\u0002J\b\u0010-\u001a\u00020\u0015H\u0002J\b\u0010.\u001a\u00020\u0015H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006/"}, d2 = {"Lcom/darrenai/jarvis/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "aiService", "Lcom/darrenai/jarvis/ai/AiService;", "binding", "Lcom/darrenai/jarvis/databinding/ActivityMainBinding;", "chatAdapter", "Lcom/darrenai/jarvis/ChatAdapter;", "currentProvider", "Lcom/darrenai/jarvis/ai/AiProvider;", "isAiResponding", "", "serviceConnection", "Landroid/content/ServiceConnection;", "tts", "Landroid/speech/tts/TextToSpeech;", "voiceService", "Lcom/darrenai/jarvis/services/VoiceListenerService;", "voiceServiceBound", "hideVoiceWaves", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onStart", "onStop", "sendMessage", "text", "", "setupChat", "setupEdgeToEdge", "setupModeIndicator", "setupProviderChip", "setupSettingsButton", "setupTTS", "setupVoice", "showProviderSelector", "showVoiceWaves", "speak", "switchProvider", "provider", "updateEmptyState", "updateProviderChip", "updateVoiceButtonState", "vibrate", "app_debug"})
public final class MainActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.darrenai.jarvis.databinding.ActivityMainBinding binding;
    private com.darrenai.jarvis.ChatAdapter chatAdapter;
    private android.speech.tts.TextToSpeech tts;
    private boolean voiceServiceBound = false;
    @org.jetbrains.annotations.Nullable
    private com.darrenai.jarvis.services.VoiceListenerService voiceService;
    private com.darrenai.jarvis.ai.AiService aiService;
    @org.jetbrains.annotations.NotNull
    private com.darrenai.jarvis.ai.AiProvider currentProvider;
    private boolean isAiResponding = false;
    @org.jetbrains.annotations.NotNull
    private final android.content.ServiceConnection serviceConnection = null;
    
    public MainActivity() {
        super();
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupEdgeToEdge() {
    }
    
    private final void setupChat() {
    }
    
    private final void setupVoice() {
    }
    
    private final void showVoiceWaves() {
    }
    
    private final void hideVoiceWaves() {
    }
    
    private final void updateVoiceButtonState() {
    }
    
    private final void setupTTS() {
    }
    
    private final void speak(java.lang.String text) {
    }
    
    private final void setupModeIndicator() {
    }
    
    private final void setupProviderChip() {
    }
    
    private final void updateProviderChip() {
    }
    
    private final void showProviderSelector() {
    }
    
    private final void switchProvider(com.darrenai.jarvis.ai.AiProvider provider) {
    }
    
    private final void setupSettingsButton() {
    }
    
    private final void updateEmptyState() {
    }
    
    private final void sendMessage(java.lang.String text) {
    }
    
    private final void vibrate() {
    }
    
    @java.lang.Override
    protected void onStart() {
    }
    
    @java.lang.Override
    protected void onStop() {
    }
    
    @java.lang.Override
    protected void onDestroy() {
    }
}