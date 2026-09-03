package com.darrenai.jarvis.ai;

/**
 * Common interface that every AI provider must implement.
 *
 * All calls are suspend functions so callers can choose their coroutine scope.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J0\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\r0\u0012H\u00a6@\u00a2\u0006\u0002\u0010\u0014J\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u00a6@\u00a2\u0006\u0002\u0010\u0017R\u0014\u0010\u0002\u001a\u00020\u00038VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0014\u0010\u0006\u001a\u00020\u00038VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\u0005R\u0012\u0010\b\u001a\u00020\tX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0018"}, d2 = {"Lcom/darrenai/jarvis/ai/IProvider;", "", "maxContextChars", "", "getMaxContextChars", "()I", "maxTokens", "getMaxTokens", "provider", "Lcom/darrenai/jarvis/ai/AiProvider;", "getProvider", "()Lcom/darrenai/jarvis/ai/AiProvider;", "chat", "", "messages", "", "Lcom/darrenai/jarvis/model/ChatMessage;", "onEvent", "Lkotlin/Function1;", "Lcom/darrenai/jarvis/ai/StreamEvent;", "(Ljava/util/List;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "healthCheck", "Lcom/darrenai/jarvis/ai/AiError;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface IProvider {
    
    /**
     * Provider metadata (id + display name).
     */
    @org.jetbrains.annotations.NotNull
    public abstract com.darrenai.jarvis.ai.AiProvider getProvider();
    
    public abstract int getMaxContextChars();
    
    public abstract int getMaxTokens();
    
    /**
     * Send [messages] to the AI and receive streaming chunks via [onEvent].
     *
     * Implementations should handle timeouts internally (30 s for OpenAI,
     * 60 s for local) and emit [StreamEvent.Error] rather than throwing.
     */
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object chat(@org.jetbrains.annotations.NotNull
    java.util.List<com.darrenai.jarvis.model.ChatMessage> messages, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.darrenai.jarvis.ai.StreamEvent, kotlin.Unit> onEvent, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Quick connectivity check — used for "Test Connection" buttons.
     * Returns null on success, an [AiError] on failure.
     */
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object healthCheck(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.darrenai.jarvis.ai.AiError> $completion);
    
    /**
     * Common interface that every AI provider must implement.
     *
     * All calls are suspend functions so callers can choose their coroutine scope.
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
        
        public static int getMaxContextChars(@org.jetbrains.annotations.NotNull
        com.darrenai.jarvis.ai.IProvider $this) {
            return 0;
        }
        
        public static int getMaxTokens(@org.jetbrains.annotations.NotNull
        com.darrenai.jarvis.ai.IProvider $this) {
            return 0;
        }
    }
}