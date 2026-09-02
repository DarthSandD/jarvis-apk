package com.darrenai.jarvis;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 \'2\u00020\u0001:\u0002\'(B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u001a\u001a\u00020\rJ\u0006\u0010\u001b\u001a\u00020\u001cJ:\u0010\u001d\u001a\u00020\u001e2\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020!0 2\u0006\u0010\"\u001a\u00020\r2\u0014\b\u0002\u0010#\u001a\u000e\u0012\u0004\u0012\u00020\u001e\u0012\u0004\u0012\u00020\u000e0\fH\u0086@\u00a2\u0006\u0002\u0010$J\u0006\u0010%\u001a\u00020\u000eJ\u0006\u0010&\u001a\u00020\u000eR\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R(\u0010\u000b\u001a\u0010\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u000e\u0018\u00010\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0015\u001a\u00020\u00168BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0019\u0010\n\u001a\u0004\b\u0017\u0010\u0018\u00a8\u0006)"}, d2 = {"Lcom/darrenai/jarvis/ConnectivityManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "connectivityManager", "Landroid/net/ConnectivityManager;", "getConnectivityManager", "()Landroid/net/ConnectivityManager;", "connectivityManager$delegate", "Lkotlin/Lazy;", "listener", "Lkotlin/Function1;", "Lcom/darrenai/jarvis/ConnectivityManager$Mode;", "", "getListener", "()Lkotlin/jvm/functions/Function1;", "setListener", "(Lkotlin/jvm/functions/Function1;)V", "networkCallback", "Landroid/net/ConnectivityManager$NetworkCallback;", "prefs", "Landroid/content/SharedPreferences;", "getPrefs", "()Landroid/content/SharedPreferences;", "prefs$delegate", "getCurrentMode", "isOnline", "", "sendMessage", "", "messages", "", "Lcom/darrenai/jarvis/model/ChatMessage;", "mode", "onProgress", "(Ljava/util/List;Lcom/darrenai/jarvis/ConnectivityManager$Mode;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "startMonitoring", "stopMonitoring", "Companion", "Mode", "app_debug"})
public final class ConnectivityManager {
    @org.jetbrains.annotations.NotNull
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy prefs$delegate = null;
    @kotlin.jvm.Volatile
    @org.jetbrains.annotations.Nullable
    private static volatile android.content.SharedPreferences _prefs;
    public static final int MAX_CONTEXT = 4096;
    public static final int MAX_TOKENS = 512;
    @org.jetbrains.annotations.Nullable
    private kotlin.jvm.functions.Function1<? super com.darrenai.jarvis.ConnectivityManager.Mode, kotlin.Unit> listener;
    @org.jetbrains.annotations.NotNull
    private final android.net.ConnectivityManager.NetworkCallback networkCallback = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy connectivityManager$delegate = null;
    @org.jetbrains.annotations.NotNull
    public static final com.darrenai.jarvis.ConnectivityManager.Companion Companion = null;
    
    public ConnectivityManager(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super();
    }
    
    private final android.content.SharedPreferences getPrefs() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final kotlin.jvm.functions.Function1<com.darrenai.jarvis.ConnectivityManager.Mode, kotlin.Unit> getListener() {
        return null;
    }
    
    public final void setListener(@org.jetbrains.annotations.Nullable
    kotlin.jvm.functions.Function1<? super com.darrenai.jarvis.ConnectivityManager.Mode, kotlin.Unit> p0) {
    }
    
    private final android.net.ConnectivityManager getConnectivityManager() {
        return null;
    }
    
    public final void startMonitoring() {
    }
    
    public final void stopMonitoring() {
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.darrenai.jarvis.ConnectivityManager.Mode getCurrentMode() {
        return null;
    }
    
    public final boolean isOnline() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object sendMessage(@org.jetbrains.annotations.NotNull
    java.util.List<com.darrenai.jarvis.model.ChatMessage> messages, @org.jetbrains.annotations.NotNull
    com.darrenai.jarvis.ConnectivityManager.Mode mode, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onProgress, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000b\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R$\u0010\n\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR$\u0010\u000f\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00048F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013\u00a8\u0006\u0014"}, d2 = {"Lcom/darrenai/jarvis/ConnectivityManager$Companion;", "", "()V", "MAX_CONTEXT", "", "MAX_TOKENS", "_prefs", "Landroid/content/SharedPreferences;", "value", "", "serverIp", "getServerIp", "()Ljava/lang/String;", "setServerIp", "(Ljava/lang/String;)V", "serverPort", "getServerPort", "()I", "setServerPort", "(I)V", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getServerIp() {
            return null;
        }
        
        public final void setServerIp(@org.jetbrains.annotations.NotNull
        java.lang.String value) {
        }
        
        public final int getServerPort() {
            return 0;
        }
        
        public final void setServerPort(int value) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2 = {"Lcom/darrenai/jarvis/ConnectivityManager$Mode;", "", "(Ljava/lang/String;I)V", "ONLINE", "OFFLINE", "UNKNOWN", "app_debug"})
    public static enum Mode {
        /*public static final*/ ONLINE /* = new ONLINE() */,
        /*public static final*/ OFFLINE /* = new OFFLINE() */,
        /*public static final*/ UNKNOWN /* = new UNKNOWN() */;
        
        Mode() {
        }
        
        @org.jetbrains.annotations.NotNull
        public static kotlin.enums.EnumEntries<com.darrenai.jarvis.ConnectivityManager.Mode> getEntries() {
            return null;
        }
    }
}