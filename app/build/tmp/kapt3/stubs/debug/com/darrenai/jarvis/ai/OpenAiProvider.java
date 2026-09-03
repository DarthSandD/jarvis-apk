package com.darrenai.jarvis.ai;

/**
 * OpenAI API provider — implements [IProvider] using the official
 * `chat/completions` endpoint with streaming (SSE) enabled.
 *
 * Falls back to a non-streaming request if the stream attempt fails
 * (e.g. proxy strips `stream=true`).
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0002\b\t\u0018\u0000 (2\u00020\u0001:\b()*+,-./B+\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\bJ0\u0010\u0010\u001a\u00020\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u00132\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020\u00110\u0016H\u0096@\u00a2\u0006\u0002\u0010\u0018J\u001c\u0010\u0019\u001a\u00020\u00032\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013H\u0082@\u00a2\u0006\u0002\u0010\u001aJ0\u0010\u001b\u001a\u00020\u001c2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u00132\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020\u00110\u0016H\u0082@\u00a2\u0006\u0002\u0010\u0018J\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0096@\u00a2\u0006\u0002\u0010\u001fJ\b\u0010 \u001a\u00020!H\u0002J\u0010\u0010\"\u001a\u00020\u00032\u0006\u0010#\u001a\u00020!H\u0002J\u001c\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00140\u00132\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013H\u0002J\u0018\u0010%\u001a\u00020\u00112\u0006\u0010#\u001a\u00020!2\u0006\u0010&\u001a\u00020\'H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00020\u0006X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0014\u0010\u0007\u001a\u00020\u0006X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\nR\u000e\u0010\u0004\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00020\r8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\u000f\u00a8\u00060"}, d2 = {"Lcom/darrenai/jarvis/ai/OpenAiProvider;", "Lcom/darrenai/jarvis/ai/IProvider;", "apiKey", "", "model", "maxContextChars", "", "maxTokens", "(Ljava/lang/String;Ljava/lang/String;II)V", "getMaxContextChars", "()I", "getMaxTokens", "provider", "Lcom/darrenai/jarvis/ai/AiProvider;", "getProvider", "()Lcom/darrenai/jarvis/ai/AiProvider;", "chat", "", "messages", "", "Lcom/darrenai/jarvis/model/ChatMessage;", "onEvent", "Lkotlin/Function1;", "Lcom/darrenai/jarvis/ai/StreamEvent;", "(Ljava/util/List;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "doNonStream", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "doStream", "", "healthCheck", "Lcom/darrenai/jarvis/ai/AiError;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "openConnection", "Ljava/net/HttpURLConnection;", "readError", "connection", "trimMessages", "writeBody", "body", "", "Companion", "DeltaContent", "NonStreamChoice", "NonStreamResponse", "OpenAiMessage", "OpenAiRequest", "StreamChoice", "StreamResponse", "app_debug"})
public final class OpenAiProvider implements com.darrenai.jarvis.ai.IProvider {
    @org.jetbrains.annotations.NotNull
    private final java.lang.String apiKey = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String model = null;
    private final int maxTokens = 0;
    private final int maxContextChars = 0;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String ENDPOINT = "https://api.openai.com/v1/chat/completions";
    private static final int CONNECT_TIMEOUT_MS = 15000;
    private static final int READ_TIMEOUT_MS = 30000;
    @org.jetbrains.annotations.NotNull
    private static final com.google.gson.Gson gson = null;
    @org.jetbrains.annotations.NotNull
    public static final com.darrenai.jarvis.ai.OpenAiProvider.Companion Companion = null;
    
    public OpenAiProvider(@org.jetbrains.annotations.NotNull
    java.lang.String apiKey, @org.jetbrains.annotations.NotNull
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
    
    private final java.lang.Object doStream(java.util.List<com.darrenai.jarvis.model.ChatMessage> messages, kotlin.jvm.functions.Function1<? super com.darrenai.jarvis.ai.StreamEvent, kotlin.Unit> onEvent, kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    private final java.lang.Object doNonStream(java.util.List<com.darrenai.jarvis.model.ChatMessage> messages, kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    private final java.net.HttpURLConnection openConnection() {
        return null;
    }
    
    private final void writeBody(java.net.HttpURLConnection connection, java.lang.Object body) {
    }
    
    private final java.lang.String readError(java.net.HttpURLConnection connection) {
        return null;
    }
    
    /**
     * Drop oldest messages until the estimated character budget fits.
     */
    private final java.util.List<com.darrenai.jarvis.model.ChatMessage> trimMessages(java.util.List<com.darrenai.jarvis.model.ChatMessage> messages) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/darrenai/jarvis/ai/OpenAiProvider$Companion;", "", "()V", "CONNECT_TIMEOUT_MS", "", "ENDPOINT", "", "READ_TIMEOUT_MS", "gson", "Lcom/google/gson/Gson;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0082\b\u0018\u00002\u00020\u0001B\u001d\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0005J\u000b\u0010\t\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\n\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J!\u0010\u000b\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001J\t\u0010\u0011\u001a\u00020\u0003H\u00d6\u0001R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007\u00a8\u0006\u0012"}, d2 = {"Lcom/darrenai/jarvis/ai/OpenAiProvider$DeltaContent;", "", "content", "", "role", "(Ljava/lang/String;Ljava/lang/String;)V", "getContent", "()Ljava/lang/String;", "getRole", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
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
        public final com.darrenai.jarvis.ai.OpenAiProvider.DeltaContent copy(@org.jetbrains.annotations.Nullable
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0082\b\u0018\u00002\u00020\u0001B\'\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\u0002\u0010\bJ\u000b\u0010\u000f\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010\u0011\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J+\u0010\u0012\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0016\u001a\u00020\u0005H\u00d6\u0001J\t\u0010\u0017\u001a\u00020\u0007H\u00d6\u0001R\u0018\u0010\u0006\u001a\u0004\u0018\u00010\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u0018"}, d2 = {"Lcom/darrenai/jarvis/ai/OpenAiProvider$NonStreamChoice;", "", "message", "Lcom/darrenai/jarvis/ai/OpenAiProvider$OpenAiMessage;", "index", "", "finishReason", "", "(Lcom/darrenai/jarvis/ai/OpenAiProvider$OpenAiMessage;ILjava/lang/String;)V", "getFinishReason", "()Ljava/lang/String;", "getIndex", "()I", "getMessage", "()Lcom/darrenai/jarvis/ai/OpenAiProvider$OpenAiMessage;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
    static final class NonStreamChoice {
        @org.jetbrains.annotations.Nullable
        private final com.darrenai.jarvis.ai.OpenAiProvider.OpenAiMessage message = null;
        private final int index = 0;
        @com.google.gson.annotations.SerializedName(value = "finish_reason")
        @org.jetbrains.annotations.Nullable
        private final java.lang.String finishReason = null;
        
        public NonStreamChoice(@org.jetbrains.annotations.Nullable
        com.darrenai.jarvis.ai.OpenAiProvider.OpenAiMessage message, int index, @org.jetbrains.annotations.Nullable
        java.lang.String finishReason) {
            super();
        }
        
        @org.jetbrains.annotations.Nullable
        public final com.darrenai.jarvis.ai.OpenAiProvider.OpenAiMessage getMessage() {
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
        public final com.darrenai.jarvis.ai.OpenAiProvider.OpenAiMessage component1() {
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
        public final com.darrenai.jarvis.ai.OpenAiProvider.NonStreamChoice copy(@org.jetbrains.annotations.Nullable
        com.darrenai.jarvis.ai.OpenAiProvider.OpenAiMessage message, int index, @org.jetbrains.annotations.Nullable
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0082\b\u0018\u00002\u00020\u0001B#\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0010\b\u0002\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0007J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u0011\u0010\r\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005H\u00c6\u0003J\'\u0010\u000e\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0010\b\u0002\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005H\u00c6\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0013H\u00d6\u0001J\t\u0010\u0014\u001a\u00020\u0003H\u00d6\u0001R\u0019\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0015"}, d2 = {"Lcom/darrenai/jarvis/ai/OpenAiProvider$NonStreamResponse;", "", "id", "", "choices", "", "Lcom/darrenai/jarvis/ai/OpenAiProvider$NonStreamChoice;", "(Ljava/lang/String;Ljava/util/List;)V", "getChoices", "()Ljava/util/List;", "getId", "()Ljava/lang/String;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
    static final class NonStreamResponse {
        @org.jetbrains.annotations.Nullable
        private final java.lang.String id = null;
        @org.jetbrains.annotations.Nullable
        private final java.util.List<com.darrenai.jarvis.ai.OpenAiProvider.NonStreamChoice> choices = null;
        
        public NonStreamResponse(@org.jetbrains.annotations.Nullable
        java.lang.String id, @org.jetbrains.annotations.Nullable
        java.util.List<com.darrenai.jarvis.ai.OpenAiProvider.NonStreamChoice> choices) {
            super();
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String getId() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.util.List<com.darrenai.jarvis.ai.OpenAiProvider.NonStreamChoice> getChoices() {
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
        public final java.util.List<com.darrenai.jarvis.ai.OpenAiProvider.NonStreamChoice> component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.darrenai.jarvis.ai.OpenAiProvider.NonStreamResponse copy(@org.jetbrains.annotations.Nullable
        java.lang.String id, @org.jetbrains.annotations.Nullable
        java.util.List<com.darrenai.jarvis.ai.OpenAiProvider.NonStreamChoice> choices) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0082\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005J\t\u0010\t\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\n\u001a\u00020\u0003H\u00c6\u0003J\u001d\u0010\u000b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001J\t\u0010\u0011\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007\u00a8\u0006\u0012"}, d2 = {"Lcom/darrenai/jarvis/ai/OpenAiProvider$OpenAiMessage;", "", "role", "", "content", "(Ljava/lang/String;Ljava/lang/String;)V", "getContent", "()Ljava/lang/String;", "getRole", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
    static final class OpenAiMessage {
        @org.jetbrains.annotations.NotNull
        private final java.lang.String role = null;
        @org.jetbrains.annotations.NotNull
        private final java.lang.String content = null;
        
        public OpenAiMessage(@org.jetbrains.annotations.NotNull
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
        public final com.darrenai.jarvis.ai.OpenAiProvider.OpenAiMessage copy(@org.jetbrains.annotations.NotNull
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0016\b\u0082\b\u0018\u00002\u00020\u0001B7\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u00a2\u0006\u0002\u0010\rJ\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\bH\u00c6\u0003J\t\u0010\u001b\u001a\u00020\nH\u00c6\u0003J\t\u0010\u001c\u001a\u00020\fH\u00c6\u0003JA\u0010\u001d\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\fH\u00c6\u0001J\u0013\u0010\u001e\u001a\u00020\f2\b\u0010\u001f\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010 \u001a\u00020\bH\u00d6\u0001J\t\u0010!\u001a\u00020\u0003H\u00d6\u0001R\u0016\u0010\u0007\u001a\u00020\b8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017\u00a8\u0006\""}, d2 = {"Lcom/darrenai/jarvis/ai/OpenAiProvider$OpenAiRequest;", "", "model", "", "messages", "", "Lcom/darrenai/jarvis/ai/OpenAiProvider$OpenAiMessage;", "maxTokens", "", "temperature", "", "stream", "", "(Ljava/lang/String;Ljava/util/List;IDZ)V", "getMaxTokens", "()I", "getMessages", "()Ljava/util/List;", "getModel", "()Ljava/lang/String;", "getStream", "()Z", "getTemperature", "()D", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
    static final class OpenAiRequest {
        @org.jetbrains.annotations.NotNull
        private final java.lang.String model = null;
        @org.jetbrains.annotations.NotNull
        private final java.util.List<com.darrenai.jarvis.ai.OpenAiProvider.OpenAiMessage> messages = null;
        @com.google.gson.annotations.SerializedName(value = "max_tokens")
        private final int maxTokens = 0;
        private final double temperature = 0.0;
        private final boolean stream = false;
        
        public OpenAiRequest(@org.jetbrains.annotations.NotNull
        java.lang.String model, @org.jetbrains.annotations.NotNull
        java.util.List<com.darrenai.jarvis.ai.OpenAiProvider.OpenAiMessage> messages, int maxTokens, double temperature, boolean stream) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getModel() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.util.List<com.darrenai.jarvis.ai.OpenAiProvider.OpenAiMessage> getMessages() {
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
        public final java.util.List<com.darrenai.jarvis.ai.OpenAiProvider.OpenAiMessage> component2() {
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
        public final com.darrenai.jarvis.ai.OpenAiProvider.OpenAiRequest copy(@org.jetbrains.annotations.NotNull
        java.lang.String model, @org.jetbrains.annotations.NotNull
        java.util.List<com.darrenai.jarvis.ai.OpenAiProvider.OpenAiMessage> messages, int maxTokens, double temperature, boolean stream) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0082\b\u0018\u00002\u00020\u0001B\'\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\u0002\u0010\bJ\u000b\u0010\u000f\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010\u0011\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J+\u0010\u0012\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0016\u001a\u00020\u0005H\u00d6\u0001J\t\u0010\u0017\u001a\u00020\u0007H\u00d6\u0001R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0018\u0010\u0006\u001a\u0004\u0018\u00010\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u0018"}, d2 = {"Lcom/darrenai/jarvis/ai/OpenAiProvider$StreamChoice;", "", "delta", "Lcom/darrenai/jarvis/ai/OpenAiProvider$DeltaContent;", "index", "", "finishReason", "", "(Lcom/darrenai/jarvis/ai/OpenAiProvider$DeltaContent;ILjava/lang/String;)V", "getDelta", "()Lcom/darrenai/jarvis/ai/OpenAiProvider$DeltaContent;", "getFinishReason", "()Ljava/lang/String;", "getIndex", "()I", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
    static final class StreamChoice {
        @org.jetbrains.annotations.Nullable
        private final com.darrenai.jarvis.ai.OpenAiProvider.DeltaContent delta = null;
        private final int index = 0;
        @com.google.gson.annotations.SerializedName(value = "finish_reason")
        @org.jetbrains.annotations.Nullable
        private final java.lang.String finishReason = null;
        
        public StreamChoice(@org.jetbrains.annotations.Nullable
        com.darrenai.jarvis.ai.OpenAiProvider.DeltaContent delta, int index, @org.jetbrains.annotations.Nullable
        java.lang.String finishReason) {
            super();
        }
        
        @org.jetbrains.annotations.Nullable
        public final com.darrenai.jarvis.ai.OpenAiProvider.DeltaContent getDelta() {
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
        public final com.darrenai.jarvis.ai.OpenAiProvider.DeltaContent component1() {
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
        public final com.darrenai.jarvis.ai.OpenAiProvider.StreamChoice copy(@org.jetbrains.annotations.Nullable
        com.darrenai.jarvis.ai.OpenAiProvider.DeltaContent delta, int index, @org.jetbrains.annotations.Nullable
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0082\b\u0018\u00002\u00020\u0001B#\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0010\b\u0002\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0007J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u0011\u0010\r\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005H\u00c6\u0003J\'\u0010\u000e\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0010\b\u0002\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005H\u00c6\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0013H\u00d6\u0001J\t\u0010\u0014\u001a\u00020\u0003H\u00d6\u0001R\u0019\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0015"}, d2 = {"Lcom/darrenai/jarvis/ai/OpenAiProvider$StreamResponse;", "", "id", "", "choices", "", "Lcom/darrenai/jarvis/ai/OpenAiProvider$StreamChoice;", "(Ljava/lang/String;Ljava/util/List;)V", "getChoices", "()Ljava/util/List;", "getId", "()Ljava/lang/String;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
    static final class StreamResponse {
        @org.jetbrains.annotations.Nullable
        private final java.lang.String id = null;
        @org.jetbrains.annotations.Nullable
        private final java.util.List<com.darrenai.jarvis.ai.OpenAiProvider.StreamChoice> choices = null;
        
        public StreamResponse(@org.jetbrains.annotations.Nullable
        java.lang.String id, @org.jetbrains.annotations.Nullable
        java.util.List<com.darrenai.jarvis.ai.OpenAiProvider.StreamChoice> choices) {
            super();
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String getId() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.util.List<com.darrenai.jarvis.ai.OpenAiProvider.StreamChoice> getChoices() {
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
        public final java.util.List<com.darrenai.jarvis.ai.OpenAiProvider.StreamChoice> component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.darrenai.jarvis.ai.OpenAiProvider.StreamResponse copy(@org.jetbrains.annotations.Nullable
        java.lang.String id, @org.jetbrains.annotations.Nullable
        java.util.List<com.darrenai.jarvis.ai.OpenAiProvider.StreamChoice> choices) {
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