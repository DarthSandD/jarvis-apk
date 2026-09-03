package com.darrenai.jarvis.ai;

/**
 * Unified AI service that exposes a single entry point to all providers.
 *
 * Responsibilities:
 * • Maintains a registry of available providers.
 * • Applies the fallback chain: selected → Hermes → Local → error.
 * • Streams partial responses to the UI via callback.
 * • Emits typed errors through [StreamEvent.Error].
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 &2\u00020\u0001:\u0002&\'B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eJ\u0016\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\n0\u000e2\u0006\u0010\u0011\u001a\u00020\u000fH\u0002J4\u0010\u0012\u001a\u00020\u00132\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\u000e2\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u000f2\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u00130\u0018J\u0006\u0010\u001a\u001a\u00020\u0013J\u001e\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\t0\u000e2\b\b\u0002\u0010\u001c\u001a\u00020\tH\u0086@\u00a2\u0006\u0002\u0010\u001dJ\u0010\u0010\u001e\u001a\u00020\n2\u0006\u0010\u001f\u001a\u00020\u000fH\u0002J\u0018\u0010 \u001a\u0004\u0018\u00010!2\u0006\u0010\u001f\u001a\u00020\u000fH\u0086@\u00a2\u0006\u0002\u0010\"J8\u0010#\u001a\u00020$2\u0006\u0010\u001f\u001a\u00020\n2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\u000e2\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u00130\u0018H\u0082@\u00a2\u0006\u0002\u0010%R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006("}, d2 = {"Lcom/darrenai/jarvis/ai/AiService;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "prefs", "Lcom/darrenai/jarvis/ai/PreferencesHelper;", "providers", "", "", "Lcom/darrenai/jarvis/ai/IProvider;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "availableProviders", "", "Lcom/darrenai/jarvis/ai/AiProvider;", "buildFallbackChain", "selected", "chat", "", "messages", "Lcom/darrenai/jarvis/model/ChatMessage;", "preferredProvider", "onEvent", "Lkotlin/Function1;", "Lcom/darrenai/jarvis/ai/StreamEvent;", "destroy", "detectLocalModels", "endpoint", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getOrCreateProvider", "provider", "healthCheck", "Lcom/darrenai/jarvis/ai/AiError;", "(Lcom/darrenai/jarvis/ai/AiProvider;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "runProviderSafely", "Lcom/darrenai/jarvis/ai/AiService$ProviderResult;", "(Lcom/darrenai/jarvis/ai/IProvider;Ljava/util/List;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "ProviderResult", "app_debug"})
public final class AiService {
    @org.jetbrains.annotations.NotNull
    private final android.content.Context context = null;
    
    /**
     * Coroutine scope tied to the lifecycle of the service.
     */
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.CoroutineScope scope = null;
    
    /**
     * User preferences (provider selection, API keys, endpoints).
     */
    @org.jetbrains.annotations.NotNull
    private final com.darrenai.jarvis.ai.PreferencesHelper prefs = null;
    
    /**
     * Cache of provider instances, keyed by [AiProvider.id].
     */
    @org.jetbrains.annotations.NotNull
    private final java.util.Map<java.lang.String, com.darrenai.jarvis.ai.IProvider> providers = null;
    @kotlin.jvm.Volatile
    @org.jetbrains.annotations.Nullable
    private static volatile com.darrenai.jarvis.ai.AiService instance;
    @org.jetbrains.annotations.NotNull
    public static final com.darrenai.jarvis.ai.AiService.Companion Companion = null;
    
    private AiService(android.content.Context context) {
        super();
    }
    
    /**
     * Send a chat message and receive streaming responses.
     *
     * @param messages The full conversation history.
     * @param preferredProvider The user's chosen provider, or null to fall back to saved preference.
     * @param onEvent Callback invoked on the main thread for each streaming event.
     */
    public final void chat(@org.jetbrains.annotations.NotNull
    java.util.List<com.darrenai.jarvis.model.ChatMessage> messages, @org.jetbrains.annotations.Nullable
    com.darrenai.jarvis.ai.AiProvider preferredProvider, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.darrenai.jarvis.ai.StreamEvent, kotlin.Unit> onEvent) {
    }
    
    /**
     * Run a connectivity check for a specific provider.
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object healthCheck(@org.jetbrains.annotations.NotNull
    com.darrenai.jarvis.ai.AiProvider provider, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.darrenai.jarvis.ai.AiError> $completion) {
        return null;
    }
    
    /**
     * Auto-detect available models on the local server.
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object detectLocalModels(@org.jetbrains.annotations.NotNull
    java.lang.String endpoint, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.String>> $completion) {
        return null;
    }
    
    /**
     * List providers that are currently usable (configured).
     */
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.darrenai.jarvis.ai.AiProvider> availableProviders() {
        return null;
    }
    
    /**
     * Cancel in-flight coroutines (call from onDestroy).
     */
    public final void destroy() {
    }
    
    private final java.lang.Object runProviderSafely(com.darrenai.jarvis.ai.IProvider provider, java.util.List<com.darrenai.jarvis.model.ChatMessage> messages, kotlin.jvm.functions.Function1<? super com.darrenai.jarvis.ai.StreamEvent, kotlin.Unit> onEvent, kotlin.coroutines.Continuation<? super com.darrenai.jarvis.ai.AiService.ProviderResult> $completion) {
        return null;
    }
    
    /**
     * Build the fallback chain. First entry is the selected provider,
     * then Hermes, then Local (if different from first two).
     */
    private final java.util.List<com.darrenai.jarvis.ai.IProvider> buildFallbackChain(com.darrenai.jarvis.ai.AiProvider selected) {
        return null;
    }
    
    /**
     * Get an existing provider instance from the cache, or create a new one.
     */
    private final com.darrenai.jarvis.ai.IProvider getOrCreateProvider(com.darrenai.jarvis.ai.AiProvider provider) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/darrenai/jarvis/ai/AiService$Companion;", "", "()V", "instance", "Lcom/darrenai/jarvis/ai/AiService;", "getInstance", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.darrenai.jarvis.ai.AiService getInstance(@org.jetbrains.annotations.NotNull
        android.content.Context context) {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b2\u0018\u00002\u00020\u0001:\u0003\u0003\u0004\u0005B\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0003\u0006\u0007\b\u00a8\u0006\t"}, d2 = {"Lcom/darrenai/jarvis/ai/AiService$ProviderResult;", "", "()V", "CompleteError", "PartialError", "Success", "Lcom/darrenai/jarvis/ai/AiService$ProviderResult$CompleteError;", "Lcom/darrenai/jarvis/ai/AiService$ProviderResult$PartialError;", "Lcom/darrenai/jarvis/ai/AiService$ProviderResult$Success;", "app_debug"})
    static abstract class ProviderResult {
        
        private ProviderResult() {
            super();
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/darrenai/jarvis/ai/AiService$ProviderResult$CompleteError;", "Lcom/darrenai/jarvis/ai/AiService$ProviderResult;", "error", "Lcom/darrenai/jarvis/ai/AiError;", "(Lcom/darrenai/jarvis/ai/AiError;)V", "getError", "()Lcom/darrenai/jarvis/ai/AiError;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
        public static final class CompleteError extends com.darrenai.jarvis.ai.AiService.ProviderResult {
            @org.jetbrains.annotations.NotNull
            private final com.darrenai.jarvis.ai.AiError error = null;
            
            public CompleteError(@org.jetbrains.annotations.NotNull
            com.darrenai.jarvis.ai.AiError error) {
            }
            
            @org.jetbrains.annotations.NotNull
            public final com.darrenai.jarvis.ai.AiError getError() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull
            public final com.darrenai.jarvis.ai.AiError component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull
            public final com.darrenai.jarvis.ai.AiService.ProviderResult.CompleteError copy(@org.jetbrains.annotations.NotNull
            com.darrenai.jarvis.ai.AiError error) {
                return null;
            }
            
            @java.lang.Override
            public boolean equals(@org.jetbrains.annotations.Nullable
            java.lang.Object other) {
                return false;
            }
            
            @java.lang.Override
            public int hashCode() {
                return 0;
            }
            
            @java.lang.Override
            @org.jetbrains.annotations.NotNull
            public java.lang.String toString() {
                return null;
            }
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/darrenai/jarvis/ai/AiService$ProviderResult$PartialError;", "Lcom/darrenai/jarvis/ai/AiService$ProviderResult;", "error", "Lcom/darrenai/jarvis/ai/AiError;", "(Lcom/darrenai/jarvis/ai/AiError;)V", "getError", "()Lcom/darrenai/jarvis/ai/AiError;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
        public static final class PartialError extends com.darrenai.jarvis.ai.AiService.ProviderResult {
            @org.jetbrains.annotations.NotNull
            private final com.darrenai.jarvis.ai.AiError error = null;
            
            public PartialError(@org.jetbrains.annotations.NotNull
            com.darrenai.jarvis.ai.AiError error) {
            }
            
            @org.jetbrains.annotations.NotNull
            public final com.darrenai.jarvis.ai.AiError getError() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull
            public final com.darrenai.jarvis.ai.AiError component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull
            public final com.darrenai.jarvis.ai.AiService.ProviderResult.PartialError copy(@org.jetbrains.annotations.NotNull
            com.darrenai.jarvis.ai.AiError error) {
                return null;
            }
            
            @java.lang.Override
            public boolean equals(@org.jetbrains.annotations.Nullable
            java.lang.Object other) {
                return false;
            }
            
            @java.lang.Override
            public int hashCode() {
                return 0;
            }
            
            @java.lang.Override
            @org.jetbrains.annotations.NotNull
            public java.lang.String toString() {
                return null;
            }
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c6\n\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0013\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u00d6\u0003J\t\u0010\u0007\u001a\u00020\bH\u00d6\u0001J\t\u0010\t\u001a\u00020\nH\u00d6\u0001\u00a8\u0006\u000b"}, d2 = {"Lcom/darrenai/jarvis/ai/AiService$ProviderResult$Success;", "Lcom/darrenai/jarvis/ai/AiService$ProviderResult;", "()V", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
        public static final class Success extends com.darrenai.jarvis.ai.AiService.ProviderResult {
            @org.jetbrains.annotations.NotNull
            public static final com.darrenai.jarvis.ai.AiService.ProviderResult.Success INSTANCE = null;
            
            private Success() {
            }
            
            @java.lang.Override
            public boolean equals(@org.jetbrains.annotations.Nullable
            java.lang.Object other) {
                return false;
            }
            
            @java.lang.Override
            public int hashCode() {
                return 0;
            }
            
            @java.lang.Override
            @org.jetbrains.annotations.NotNull
            public java.lang.String toString() {
                return null;
            }
        }
    }
}