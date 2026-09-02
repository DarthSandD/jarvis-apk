package com.darrenai.jarvis.services;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\t\u0018\u0000 \u001e2\u00020\u0001:\u0002\u001e\u001fB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\r\u001a\u00020\u000eH\u0002J\b\u0010\u000f\u001a\u00020\nH\u0002J\u0012\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0016J\b\u0010\u0014\u001a\u00020\nH\u0016J\b\u0010\u0015\u001a\u00020\nH\u0016J\"\u0010\u0016\u001a\u00020\u00172\b\u0010\u0012\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\u0017H\u0016J\u0012\u0010\u001a\u001a\u00020\u00062\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0016J\u001a\u0010\u001b\u001a\u00020\n2\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\bJ\u0006\u0010\u001d\u001a\u00020\nR\u0012\u0010\u0003\u001a\u00060\u0004R\u00020\u0000X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0007\u001a\u0010\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lcom/darrenai/jarvis/services/VoiceListenerService;", "Landroid/app/Service;", "()V", "binder", "Lcom/darrenai/jarvis/services/VoiceListenerService$LocalBinder;", "listening", "", "onTranscript", "Lkotlin/Function1;", "", "", "speechRecognizer", "Landroid/speech/SpeechRecognizer;", "buildNotification", "Landroid/app/Notification;", "createNotificationChannel", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onCreate", "onDestroy", "onStartCommand", "", "flags", "startId", "onUnbind", "startListening", "callback", "stopListening", "Companion", "LocalBinder", "app_debug"})
public final class VoiceListenerService extends android.app.Service {
    @org.jetbrains.annotations.NotNull
    private final com.darrenai.jarvis.services.VoiceListenerService.LocalBinder binder = null;
    private boolean listening = false;
    @org.jetbrains.annotations.Nullable
    private android.speech.SpeechRecognizer speechRecognizer;
    @org.jetbrains.annotations.Nullable
    private kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onTranscript;
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String CHANNEL_ID = "jarvis_voice";
    public static final int NOTIFICATION_ID = 1;
    public static final int REQUEST_VOICE = 1001;
    @kotlin.jvm.Volatile
    private static volatile boolean isListening = false;
    @org.jetbrains.annotations.Nullable
    private static com.darrenai.jarvis.services.VoiceListenerService instance;
    @org.jetbrains.annotations.NotNull
    public static final com.darrenai.jarvis.services.VoiceListenerService.Companion Companion = null;
    
    public VoiceListenerService() {
        super();
    }
    
    @java.lang.Override
    public void onCreate() {
    }
    
    @java.lang.Override
    public int onStartCommand(@org.jetbrains.annotations.Nullable
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public android.os.IBinder onBind(@org.jetbrains.annotations.Nullable
    android.content.Intent intent) {
        return null;
    }
    
    @java.lang.Override
    public boolean onUnbind(@org.jetbrains.annotations.Nullable
    android.content.Intent intent) {
        return false;
    }
    
    @java.lang.Override
    public void onDestroy() {
    }
    
    private final void createNotificationChannel() {
    }
    
    public final void startListening(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> callback) {
    }
    
    public final void stopListening() {
    }
    
    private final android.app.Notification buildNotification() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u000f\u001a\u00020\tR\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\f\"\u0004\b\r\u0010\u000e\u00a8\u0006\u0010"}, d2 = {"Lcom/darrenai/jarvis/services/VoiceListenerService$Companion;", "", "()V", "CHANNEL_ID", "", "NOTIFICATION_ID", "", "REQUEST_VOICE", "instance", "Lcom/darrenai/jarvis/services/VoiceListenerService;", "isListening", "", "()Z", "setListening", "(Z)V", "getInstance", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final boolean isListening() {
            return false;
        }
        
        public final void setListening(boolean p0) {
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.darrenai.jarvis.services.VoiceListenerService getInstance() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/darrenai/jarvis/services/VoiceListenerService$LocalBinder;", "Landroid/os/Binder;", "(Lcom/darrenai/jarvis/services/VoiceListenerService;)V", "service", "Lcom/darrenai/jarvis/services/VoiceListenerService;", "app_debug"})
    public final class LocalBinder extends android.os.Binder {
        
        public LocalBinder() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.darrenai.jarvis.services.VoiceListenerService service() {
            return null;
        }
    }
}