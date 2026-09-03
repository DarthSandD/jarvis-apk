package com.darrenai.jarvis.ai;

/**
 * Hermes OmniRoute provider — wraps the existing [ConnectivityManager]
 * implementation so it conforms to the unified [IProvider] interface.
 *
 * Uses the current Hermes backend at the configured IP/port.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 !2\u00020\u0001:\u0001!B5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\nJ0\u0010\u0014\u001a\u00020\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u00172\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u001b\u0012\u0004\u0012\u00020\u00150\u001aH\u0096@\u00a2\u0006\u0002\u0010\u001cJ\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0096@\u00a2\u0006\u0002\u0010\u001fJ\u001c\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00180\u00172\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017H\u0002R\u0016\u0010\u000b\u001a\n \f*\u0004\u0018\u00010\u00030\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00020\u0007X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0014\u0010\b\u001a\u00020\u0007X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000eR\u0014\u0010\u0010\u001a\u00020\u00118VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2 = {"Lcom/darrenai/jarvis/ai/HermesProvider;", "Lcom/darrenai/jarvis/ai/IProvider;", "context", "Landroid/content/Context;", "serverIp", "", "serverPort", "", "maxTokens", "maxContextChars", "(Landroid/content/Context;Ljava/lang/String;III)V", "appContext", "kotlin.jvm.PlatformType", "getMaxContextChars", "()I", "getMaxTokens", "provider", "Lcom/darrenai/jarvis/ai/AiProvider;", "getProvider", "()Lcom/darrenai/jarvis/ai/AiProvider;", "chat", "", "messages", "", "Lcom/darrenai/jarvis/model/ChatMessage;", "onEvent", "Lkotlin/Function1;", "Lcom/darrenai/jarvis/ai/StreamEvent;", "(Ljava/util/List;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "healthCheck", "Lcom/darrenai/jarvis/ai/AiError;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "trimMessages", "Companion", "app_debug"})
public final class HermesProvider implements com.darrenai.jarvis.ai.IProvider {
    @org.jetbrains.annotations.NotNull
    private final java.lang.String serverIp = null;
    private final int serverPort = 0;
    private final int maxContextChars = 0;
    private final int maxTokens = 0;
    private static final int CONNECT_TIMEOUT_MS = 15000;
    private static final int READ_TIMEOUT_MS = 30000;
    private final android.content.Context appContext = null;
    @org.jetbrains.annotations.NotNull
    public static final com.darrenai.jarvis.ai.HermesProvider.Companion Companion = null;
    
    public HermesProvider(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    java.lang.String serverIp, int serverPort, int maxTokens, int maxContextChars) {
        super();
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public com.darrenai.jarvis.ai.AiProvider getProvider() {
        return null;
    }
    
    @java.lang.Override
    public int getMaxContextChars() {
        return 0;
    }
    
    @java.lang.Override
    public int getMaxTokens() {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object chat(@org.jetbrains.annotations.NotNull
    java.util.List<com.darrenai.jarvis.model.ChatMessage> messages, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.darrenai.jarvis.ai.StreamEvent, kotlin.Unit> onEvent, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object healthCheck(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.darrenai.jarvis.ai.AiError> $completion) {
        return null;
    }
    
    private final java.util.List<com.darrenai.jarvis.model.ChatMessage> trimMessages(java.util.List<com.darrenai.jarvis.model.ChatMessage> messages) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/darrenai/jarvis/ai/HermesProvider$Companion;", "", "()V", "CONNECT_TIMEOUT_MS", "", "READ_TIMEOUT_MS", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}