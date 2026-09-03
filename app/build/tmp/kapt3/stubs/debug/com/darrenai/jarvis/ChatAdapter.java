package com.darrenai.jarvis;

/**
 * Modern chat adapter — renders user, assistant, and system messages
 * with asymmetric bubbles, timestamps, and avatar indicators.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0001\"B\u0005\u00a2\u0006\u0002\u0010\u0003J\u000e\u0010\f\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u0006J\u0006\u0010\u000e\u001a\u00020\tJ\b\u0010\u000f\u001a\u00020\u0010H\u0016J\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0012\u001a\u00020\u0010J\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00060\u0014J\u001c\u0010\u0015\u001a\u00020\t2\n\u0010\u0016\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0017\u001a\u00020\u0010H\u0016J\u001c\u0010\u0018\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u0010H\u0016J\u000e\u0010\u001c\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\u0010J\u0014\u0010\u001d\u001a\u00020\t2\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\t0\bJ\u0016\u0010\u001f\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\u00102\u0006\u0010 \u001a\u00020!R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/darrenai/jarvis/ChatAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/darrenai/jarvis/ChatAdapter$MessageViewHolder;", "()V", "messages", "", "Lcom/darrenai/jarvis/model/ChatMessage;", "onNewMessageListener", "Lkotlin/Function0;", "", "timeFormat", "Ljava/text/SimpleDateFormat;", "addMessage", "msg", "clear", "getItemCount", "", "getMessage", "index", "getMessagesForAi", "", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "removeMessageAt", "setOnNewMessageListener", "listener", "updateMessage", "text", "", "MessageViewHolder", "app_debug"})
public final class ChatAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.darrenai.jarvis.ChatAdapter.MessageViewHolder> {
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.darrenai.jarvis.model.ChatMessage> messages = null;
    @org.jetbrains.annotations.Nullable
    private kotlin.jvm.functions.Function0<kotlin.Unit> onNewMessageListener;
    @org.jetbrains.annotations.NotNull
    private final java.text.SimpleDateFormat timeFormat = null;
    
    public ChatAdapter() {
        super();
    }
    
    public final void setOnNewMessageListener(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> listener) {
    }
    
    public final void addMessage(@org.jetbrains.annotations.NotNull
    com.darrenai.jarvis.model.ChatMessage msg) {
    }
    
    public final void updateMessage(int index, @org.jetbrains.annotations.NotNull
    java.lang.String text) {
    }
    
    public final void removeMessageAt(int index) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.darrenai.jarvis.model.ChatMessage> getMessagesForAi() {
        return null;
    }
    
    public final void clear() {
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.darrenai.jarvis.model.ChatMessage getMessage(int index) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public com.darrenai.jarvis.ChatAdapter.MessageViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull
    com.darrenai.jarvis.ChatAdapter.MessageViewHolder holder, int position) {
    }
    
    @java.lang.Override
    public int getItemCount() {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/darrenai/jarvis/ChatAdapter$MessageViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/darrenai/jarvis/databinding/ItemChatMessageBinding;", "(Lcom/darrenai/jarvis/ChatAdapter;Lcom/darrenai/jarvis/databinding/ItemChatMessageBinding;)V", "bind", "", "msg", "Lcom/darrenai/jarvis/model/ChatMessage;", "position", "", "app_debug"})
    public final class MessageViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull
        private final com.darrenai.jarvis.databinding.ItemChatMessageBinding binding = null;
        
        public MessageViewHolder(@org.jetbrains.annotations.NotNull
        com.darrenai.jarvis.databinding.ItemChatMessageBinding binding) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull
        com.darrenai.jarvis.model.ChatMessage msg, int position) {
        }
    }
}