package com.darrenai.jarvis.ai;

/**
 * Local AI provider — communicates with a [llama.cpp](https://github.com/ggerganov/llama.cpp)
 * server that exposes the OpenAI-compatible `/v1/chat/completions` endpoint.
 *
 * The endpoint URL and model name are fully configurable via [PreferencesHelper].
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0002\b\u000b\u0018\u0000 +2\u00020\u0001:\n+,-./01234B-\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\bJ0\u0010\u0010\u001a\u00020\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u00132\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020\u00110\u0016H\u0096@\u00a2\u0006\u0002\u0010\u0018J\u0014\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00030\u0013H\u0086@\u00a2\u0006\u0002\u0010\u001aJ$\u0010\u001b\u001a\u00020\u00032\u0006\u0010\u001c\u001a\u00020\u00032\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013H\u0082@\u00a2\u0006\u0002\u0010\u001dJ8\u0010\u001e\u001a\u00020\u001f2\u0006\u0010\u001c\u001a\u00020\u00032\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u00132\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020\u00110\u0016H\u0082@\u00a2\u0006\u0002\u0010 J\u0010\u0010!\u001a\u0004\u0018\u00010\"H\u0096@\u00a2\u0006\u0002\u0010\u001aJ\u0010\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020\u0003H\u0002J\u001c\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00140\u00132\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013H\u0002J\u0018\u0010\'\u001a\u00020\u00112\u0006\u0010(\u001a\u00020$2\u0006\u0010)\u001a\u00020*H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00020\u0006X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0014\u0010\u0007\u001a\u00020\u0006X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\nR\u000e\u0010\u0004\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00020\r8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\u000f\u00a8\u00065"}, d2 = {"Lcom/darrenai/jarvis/ai/LocalAiProvider;", "Lcom/darrenai/jarvis/ai/IProvider;", "endpoint", "", "model", "maxContextChars", "", "maxTokens", "(Ljava/lang/String;Ljava/lang/String;II)V", "getMaxContextChars", "()I", "getMaxTokens", "provider", "Lcom/darrenai/jarvis/ai/AiProvider;", "getProvider", "()Lcom/darrenai/jarvis/ai/AiProvider;", "chat", "", "messages", "", "Lcom/darrenai/jarvis/model/ChatMessage;", "onEvent", "Lkotlin/Function1;", "Lcom/darrenai/jarvis/ai/StreamEvent;", "(Ljava/util/List;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "detectModels", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "doNonStream", "completionsUrl", "(Ljava/lang/String;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "doStream", "", "(Ljava/lang/String;Ljava/util/List;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "healthCheck", "Lcom/darrenai/jarvis/ai/AiError;", "openConnection", "Ljava/net/HttpURLConnection;", "url", "trimMessages", "writeBody", "connection", "body", "", "Companion", "DeltaContent", "LlamaMessage", "LlamaRequest", "ModelInfo", "ModelsResponse", "NonStreamChoice", "NonStreamResponse", "StreamChoice", "StreamResponse", "app_debug"})
public final class LocalAiProvider implements com.darrenai.jarvis.ai.IProvider {
    @org.jetbrains.annotations.NotNull
    private final java.lang.String endpoint = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String model = null;
    private final int maxTokens = 0;
    private final int maxContextChars = 0;
    private static final int CONNECT_TIMEOUT_MS = 10000;
    private static final int READ_TIMEOUT_MS = 60000;
    @org.jetbrains.annotations.NotNull
    private static final com.google.gson.Gson gson = null;
    @org.jetbrains.annotations.NotNull
    public static final com.darrenai.jarvis.ai.LocalAiProvider.Companion Companion = null;
    
    public LocalAiProvider(@org.jetbrains.annotations.NotNull
    java.lang.String endpoint, @org.jetbrains.annotations.NotNull
    java.lang.String model, int maxContextChars, int maxTokens) {
        super();
    }
    
    @java.lang.Override
    public int getMaxTokens() {
        return 0;
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
    
    /**
     * Auto-detect available models by querying `/v1/models`.
     * Returns a list of model IDs, or empty list if the endpoint is unreachable.
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object detectModels(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.String>> $completion) {
        return null;
    }
    
    private final java.lang.Object doStream(java.lang.String completionsUrl, java.util.List<com.darrenai.jarvis.model.ChatMessage> messages, kotlin.jvm.functions.Function1<? super com.darrenai.jarvis.ai.StreamEvent, kotlin.Unit> onEvent, kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    private final java.lang.Object doNonStream(java.lang.String completionsUrl, java.util.List<com.darrenai.jarvis.model.ChatMessage> messages, kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    private final java.net.HttpURLConnection openConnection(java.lang.String url) {
        return null;
    }
    
    private final void writeBody(java.net.HttpURLConnection connection, java.lang.Object body) {
    }
    
    private final java.util.List<com.darrenai.jarvis.model.ChatMessage> trimMessages(java.util.List<com.darrenai.jarvis.model.ChatMessage> messages) {
        return null;
    }
    
    public LocalAiProvider() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\tR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/darrenai/jarvis/ai/LocalAiProvider$Companion;", "", "()V", "CONNECT_TIMEOUT_MS", "", "READ_TIMEOUT_MS", "gson", "Lcom/google/gson/Gson;", "normalizeEndpoint", "", "raw", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        /**
         * Normalize an endpoint: strip trailing slashes and ensure /chat/completions suffix.
         */
        @org.jetbrains.annotations.NotNull
        public final java.lang.String normalizeEndpoint(@org.jetbrains.annotations.NotNull
        java.lang.String raw) {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0082\b\u0018\u00002\u00020\u0001B\u001d\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0005J\u000b\u0010\t\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\n\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J!\u0010\u000b\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001J\t\u0010\u0011\u001a\u00020\u0003H\u00d6\u0001R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007\u00a8\u0006\u0012"}, d2 = {"Lcom/darrenai/jarvis/ai/LocalAiProvider$DeltaContent;", "", "content", "", "role", "(Ljava/lang/String;Ljava/lang/String;)V", "getContent", "()Ljava/lang/String;", "getRole", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
    static final class DeltaContent {
        @org.jetbrains.annotations.Nullable
        private final java.lang.String content = null;
        @org.jetbrains.annotations.Nullable
        private final java.lang.String role = null;
        
        public DeltaContent(@org.jetbrains.annotations.Nullable
        java.lang.String content, @org.jetbrains.annotations.Nullable
        java.lang.String role) {
            super();
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String getContent() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String getRole() {
            return null;
        }
        
        public DeltaContent() {
            super();
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.darrenai.jarvis.ai.LocalAiProvider.DeltaContent copy(@org.jetbrains.annotations.Nullable
        java.lang.String content, @org.jetbrains.annotations.Nullable
        java.lang.String role) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0082\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005J\t\u0010\t\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\n\u001a\u00020\u0003H\u00c6\u0003J\u001d\u0010\u000b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001J\t\u0010\u0011\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007\u00a8\u0006\u0012"}, d2 = {"Lcom/darrenai/jarvis/ai/LocalAiProvider$LlamaMessage;", "", "role", "", "content", "(Ljava/lang/String;Ljava/lang/String;)V", "getContent", "()Ljava/lang/String;", "getRole", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
    static final class LlamaMessage {
        @org.jetbrains.annotations.NotNull
        private final java.lang.String role = null;
        @org.jetbrains.annotations.NotNull
        private final java.lang.String content = null;
        
        public LlamaMessage(@org.jetbrains.annotations.NotNull
        java.lang.String role, @org.jetbrains.annotations.NotNull
        java.lang.String content) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getRole() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getContent() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.darrenai.jarvis.ai.LocalAiProvider.LlamaMessage copy(@org.jetbrains.annotations.NotNull
        java.lang.String role, @org.jetbrains.annotations.NotNull
        java.lang.String content) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0016\b\u0082\b\u0018\u00002\u00020\u0001B7\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u00a2\u0006\u0002\u0010\rJ\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\bH\u00c6\u0003J\t\u0010\u001b\u001a\u00020\nH\u00c6\u0003J\t\u0010\u001c\u001a\u00020\fH\u00c6\u0003JA\u0010\u001d\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\fH\u00c6\u0001J\u0013\u0010\u001e\u001a\u00020\f2\b\u0010\u001f\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010 \u001a\u00020\bH\u00d6\u0001J\t\u0010!\u001a\u00020\u0003H\u00d6\u0001R\u0016\u0010\u0007\u001a\u00020\b8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017\u00a8\u0006\""}, d2 = {"Lcom/darrenai/jarvis/ai/LocalAiProvider$LlamaRequest;", "", "model", "", "messages", "", "Lcom/darrenai/jarvis/ai/LocalAiProvider$LlamaMessage;", "maxTokens", "", "temperature", "", "stream", "", "(Ljava/lang/String;Ljava/util/List;IDZ)V", "getMaxTokens", "()I", "getMessages", "()Ljava/util/List;", "getModel", "()Ljava/lang/String;", "getStream", "()Z", "getTemperature", "()D", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
    static final class LlamaRequest {
        @org.jetbrains.annotations.NotNull
        private final java.lang.String model = null;
        @org.jetbrains.annotations.NotNull
        private final java.util.List<com.darrenai.jarvis.ai.LocalAiProvider.LlamaMessage> messages = null;
        @com.google.gson.annotations.SerializedName(value = "max_tokens")
        private final int maxTokens = 0;
        private final double temperature = 0.0;
        private final boolean stream = false;
        
        public LlamaRequest(@org.jetbrains.annotations.NotNull
        java.lang.String model, @org.jetbrains.annotations.NotNull
        java.util.List<com.darrenai.jarvis.ai.LocalAiProvider.LlamaMessage> messages, int maxTokens, double temperature, boolean stream) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getModel() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.util.List<com.darrenai.jarvis.ai.LocalAiProvider.LlamaMessage> getMessages() {
            return null;
        }
        
        public final int getMaxTokens() {
            return 0;
        }
        
        public final double getTemperature() {
            return 0.0;
        }
        
        public final boolean getStream() {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.util.List<com.darrenai.jarvis.ai.LocalAiProvider.LlamaMessage> component2() {
            return null;
        }
        
        public final int component3() {
            return 0;
        }
        
        public final double component4() {
            return 0.0;
        }
        
        public final boolean component5() {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.darrenai.jarvis.ai.LocalAiProvider.LlamaRequest copy(@org.jetbrains.annotations.NotNull
        java.lang.String model, @org.jetbrains.annotations.NotNull
        java.util.List<com.darrenai.jarvis.ai.LocalAiProvider.LlamaMessage> messages, int maxTokens, double temperature, boolean stream) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0082\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\f\u001a\u00020\rH\u00d6\u0001J\t\u0010\u000e\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u000f"}, d2 = {"Lcom/darrenai/jarvis/ai/LocalAiProvider$ModelInfo;", "", "id", "", "(Ljava/lang/String;)V", "getId", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
    static final class ModelInfo {
        @org.jetbrains.annotations.NotNull
        private final java.lang.String id = null;
        
        public ModelInfo(@org.jetbrains.annotations.NotNull
        java.lang.String id) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getId() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.darrenai.jarvis.ai.LocalAiProvider.ModelInfo copy(@org.jetbrains.annotations.NotNull
        java.lang.String id) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0010\b\u0002\u0010\u0002\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0005J\u0011\u0010\b\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0003H\u00c6\u0003J\u001b\u0010\t\u001a\u00020\u00002\u0010\b\u0002\u0010\u0002\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0019\u0010\u0002\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0011"}, d2 = {"Lcom/darrenai/jarvis/ai/LocalAiProvider$ModelsResponse;", "", "data", "", "Lcom/darrenai/jarvis/ai/LocalAiProvider$ModelInfo;", "(Ljava/util/List;)V", "getData", "()Ljava/util/List;", "component1", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"})
    static final class ModelsResponse {
        @org.jetbrains.annotations.Nullable
        private final java.util.List<com.darrenai.jarvis.ai.LocalAiProvider.ModelInfo> data = null;
        
        public ModelsResponse(@org.jetbrains.annotations.Nullable
        java.util.List<com.darrenai.jarvis.ai.LocalAiProvider.ModelInfo> data) {
            super();
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.util.List<com.darrenai.jarvis.ai.LocalAiProvider.ModelInfo> getData() {
            return null;
        }
        
        public ModelsResponse() {
            super();
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.util.List<com.darrenai.jarvis.ai.LocalAiProvider.ModelInfo> component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.darrenai.jarvis.ai.LocalAiProvider.ModelsResponse copy(@org.jetbrains.annotations.Nullable
        java.util.List<com.darrenai.jarvis.ai.LocalAiProvider.ModelInfo> data) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0082\b\u0018\u00002\u00020\u0001B\'\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\u0002\u0010\bJ\u000b\u0010\u000f\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010\u0011\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J+\u0010\u0012\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0016\u001a\u00020\u0005H\u00d6\u0001J\t\u0010\u0017\u001a\u00020\u0007H\u00d6\u0001R\u0018\u0010\u0006\u001a\u0004\u0018\u00010\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u0018"}, d2 = {"Lcom/darrenai/jarvis/ai/LocalAiProvider$NonStreamChoice;", "", "message", "Lcom/darrenai/jarvis/ai/LocalAiProvider$LlamaMessage;", "index", "", "finishReason", "", "(Lcom/darrenai/jarvis/ai/LocalAiProvider$LlamaMessage;ILjava/lang/String;)V", "getFinishReason", "()Ljava/lang/String;", "getIndex", "()I", "getMessage", "()Lcom/darrenai/jarvis/ai/LocalAiProvider$LlamaMessage;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
    static final class NonStreamChoice {
        @org.jetbrains.annotations.Nullable
        private final com.darrenai.jarvis.ai.LocalAiProvider.LlamaMessage message = null;
        private final int index = 0;
        @com.google.gson.annotations.SerializedName(value = "finish_reason")
        @org.jetbrains.annotations.Nullable
        private final java.lang.String finishReason = null;
        
        public NonStreamChoice(@org.jetbrains.annotations.Nullable
        com.darrenai.jarvis.ai.LocalAiProvider.LlamaMessage message, int index, @org.jetbrains.annotations.Nullable
        java.lang.String finishReason) {
            super();
        }
        
        @org.jetbrains.annotations.Nullable
        public final com.darrenai.jarvis.ai.LocalAiProvider.LlamaMessage getMessage() {
            return null;
        }
        
        public final int getIndex() {
            return 0;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String getFinishReason() {
            return null;
        }
        
        public NonStreamChoice() {
            super();
        }
        
        @org.jetbrains.annotations.Nullable
        public final com.darrenai.jarvis.ai.LocalAiProvider.LlamaMessage component1() {
            return null;
        }
        
        public final int component2() {
            return 0;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String component3() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.darrenai.jarvis.ai.LocalAiProvider.NonStreamChoice copy(@org.jetbrains.annotations.Nullable
        com.darrenai.jarvis.ai.LocalAiProvider.LlamaMessage message, int index, @org.jetbrains.annotations.Nullable
        java.lang.String finishReason) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0082\b\u0018\u00002\u00020\u0001B#\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0010\b\u0002\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0007J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u0011\u0010\r\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005H\u00c6\u0003J\'\u0010\u000e\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0010\b\u0002\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005H\u00c6\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0013H\u00d6\u0001J\t\u0010\u0014\u001a\u00020\u0003H\u00d6\u0001R\u0019\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0015"}, d2 = {"Lcom/darrenai/jarvis/ai/LocalAiProvider$NonStreamResponse;", "", "id", "", "choices", "", "Lcom/darrenai/jarvis/ai/LocalAiProvider$NonStreamChoice;", "(Ljava/lang/String;Ljava/util/List;)V", "getChoices", "()Ljava/util/List;", "getId", "()Ljava/lang/String;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
    static final class NonStreamResponse {
        @org.jetbrains.annotations.Nullable
        private final java.lang.String id = null;
        @org.jetbrains.annotations.Nullable
        private final java.util.List<com.darrenai.jarvis.ai.LocalAiProvider.NonStreamChoice> choices = null;
        
        public NonStreamResponse(@org.jetbrains.annotations.Nullable
        java.lang.String id, @org.jetbrains.annotations.Nullable
        java.util.List<com.darrenai.jarvis.ai.LocalAiProvider.NonStreamChoice> choices) {
            super();
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String getId() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.util.List<com.darrenai.jarvis.ai.LocalAiProvider.NonStreamChoice> getChoices() {
            return null;
        }
        
        public NonStreamResponse() {
            super();
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.util.List<com.darrenai.jarvis.ai.LocalAiProvider.NonStreamChoice> component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.darrenai.jarvis.ai.LocalAiProvider.NonStreamResponse copy(@org.jetbrains.annotations.Nullable
        java.lang.String id, @org.jetbrains.annotations.Nullable
        java.util.List<com.darrenai.jarvis.ai.LocalAiProvider.NonStreamChoice> choices) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0082\b\u0018\u00002\u00020\u0001B\'\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\u0002\u0010\bJ\u000b\u0010\u000f\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010\u0011\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J+\u0010\u0012\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0016\u001a\u00020\u0005H\u00d6\u0001J\t\u0010\u0017\u001a\u00020\u0007H\u00d6\u0001R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0018\u0010\u0006\u001a\u0004\u0018\u00010\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u0018"}, d2 = {"Lcom/darrenai/jarvis/ai/LocalAiProvider$StreamChoice;", "", "delta", "Lcom/darrenai/jarvis/ai/LocalAiProvider$DeltaContent;", "index", "", "finishReason", "", "(Lcom/darrenai/jarvis/ai/LocalAiProvider$DeltaContent;ILjava/lang/String;)V", "getDelta", "()Lcom/darrenai/jarvis/ai/LocalAiProvider$DeltaContent;", "getFinishReason", "()Ljava/lang/String;", "getIndex", "()I", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
    static final class StreamChoice {
        @org.jetbrains.annotations.Nullable
        private final com.darrenai.jarvis.ai.LocalAiProvider.DeltaContent delta = null;
        private final int index = 0;
        @com.google.gson.annotations.SerializedName(value = "finish_reason")
        @org.jetbrains.annotations.Nullable
        private final java.lang.String finishReason = null;
        
        public StreamChoice(@org.jetbrains.annotations.Nullable
        com.darrenai.jarvis.ai.LocalAiProvider.DeltaContent delta, int index, @org.jetbrains.annotations.Nullable
        java.lang.String finishReason) {
            super();
        }
        
        @org.jetbrains.annotations.Nullable
        public final com.darrenai.jarvis.ai.LocalAiProvider.DeltaContent getDelta() {
            return null;
        }
        
        public final int getIndex() {
            return 0;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String getFinishReason() {
            return null;
        }
        
        public StreamChoice() {
            super();
        }
        
        @org.jetbrains.annotations.Nullable
        public final com.darrenai.jarvis.ai.LocalAiProvider.DeltaContent component1() {
            return null;
        }
        
        public final int component2() {
            return 0;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String component3() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.darrenai.jarvis.ai.LocalAiProvider.StreamChoice copy(@org.jetbrains.annotations.Nullable
        com.darrenai.jarvis.ai.LocalAiProvider.DeltaContent delta, int index, @org.jetbrains.annotations.Nullable
        java.lang.String finishReason) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0082\b\u0018\u00002\u00020\u0001B#\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0010\b\u0002\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0007J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u0011\u0010\r\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005H\u00c6\u0003J\'\u0010\u000e\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0010\b\u0002\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005H\u00c6\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0013H\u00d6\u0001J\t\u0010\u0014\u001a\u00020\u0003H\u00d6\u0001R\u0019\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0015"}, d2 = {"Lcom/darrenai/jarvis/ai/LocalAiProvider$StreamResponse;", "", "id", "", "choices", "", "Lcom/darrenai/jarvis/ai/LocalAiProvider$StreamChoice;", "(Ljava/lang/String;Ljava/util/List;)V", "getChoices", "()Ljava/util/List;", "getId", "()Ljava/lang/String;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
    static final class StreamResponse {
        @org.jetbrains.annotations.Nullable
        private final java.lang.String id = null;
        @org.jetbrains.annotations.Nullable
        private final java.util.List<com.darrenai.jarvis.ai.LocalAiProvider.StreamChoice> choices = null;
        
        public StreamResponse(@org.jetbrains.annotations.Nullable
        java.lang.String id, @org.jetbrains.annotations.Nullable
        java.util.List<com.darrenai.jarvis.ai.LocalAiProvider.StreamChoice> choices) {
            super();
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String getId() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.util.List<com.darrenai.jarvis.ai.LocalAiProvider.StreamChoice> getChoices() {
            return null;
        }
        
        public StreamResponse() {
            super();
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.util.List<com.darrenai.jarvis.ai.LocalAiProvider.StreamChoice> component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.darrenai.jarvis.ai.LocalAiProvider.StreamResponse copy(@org.jetbrains.annotations.Nullable
        java.lang.String id, @org.jetbrains.annotations.Nullable
        java.util.List<com.darrenai.jarvis.ai.LocalAiProvider.StreamChoice> choices) {
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
}