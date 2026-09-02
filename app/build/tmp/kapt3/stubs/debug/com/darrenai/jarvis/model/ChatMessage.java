package com.darrenai.jarvis.model;

/**
 * Single chat message used across the app.
 * Placed in its own file so both MainActivity and ChatAdapter can import it
 * without the circular dependency that would arise from keeping it inside
 * ConnectivityManager.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\b\u0018\u00002\u00020\u0001:\u0001\u0019B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\t\u0010\u000f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0011\u001a\u00020\u0007H\u00c6\u0003J\'\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0016\u001a\u00020\u0017H\u00d6\u0001J\t\u0010\u0018\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u001a"}, d2 = {"Lcom/darrenai/jarvis/model/ChatMessage;", "", "role", "Lcom/darrenai/jarvis/model/ChatMessage$Role;", "content", "", "timestamp", "", "(Lcom/darrenai/jarvis/model/ChatMessage$Role;Ljava/lang/String;J)V", "getContent", "()Ljava/lang/String;", "getRole", "()Lcom/darrenai/jarvis/model/ChatMessage$Role;", "getTimestamp", "()J", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "Role", "app_debug"})
public final class ChatMessage {
    @org.jetbrains.annotations.NotNull
    private final com.darrenai.jarvis.model.ChatMessage.Role role = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String content = null;
    private final long timestamp = 0L;
    
    public ChatMessage(@org.jetbrains.annotations.NotNull
    com.darrenai.jarvis.model.ChatMessage.Role role, @org.jetbrains.annotations.NotNull
    java.lang.String content, long timestamp) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.darrenai.jarvis.model.ChatMessage.Role getRole() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getContent() {
        return null;
    }
    
    public final long getTimestamp() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.darrenai.jarvis.model.ChatMessage.Role component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component2() {
        return null;
    }
    
    public final long component3() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.darrenai.jarvis.model.ChatMessage copy(@org.jetbrains.annotations.NotNull
    com.darrenai.jarvis.model.ChatMessage.Role role, @org.jetbrains.annotations.NotNull
    java.lang.String content, long timestamp) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2 = {"Lcom/darrenai/jarvis/model/ChatMessage$Role;", "", "(Ljava/lang/String;I)V", "USER", "ASSISTANT", "SYSTEM", "app_debug"})
    public static enum Role {
        /*public static final*/ USER /* = new USER() */,
        /*public static final*/ ASSISTANT /* = new ASSISTANT() */,
        /*public static final*/ SYSTEM /* = new SYSTEM() */;
        
        Role() {
        }
        
        @org.jetbrains.annotations.NotNull
        public static kotlin.enums.EnumEntries<com.darrenai.jarvis.model.ChatMessage.Role> getEntries() {
            return null;
        }
    }
}