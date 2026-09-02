package com.darrenai.jarvis;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u0000 \u000e2\u00020\u0001:\u0001\u000eB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\f\u001a\u00020\rH\u0016R\u001e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u0086.\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u001e\u0010\t\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\b@BX\u0086.\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u000f"}, d2 = {"Lcom/darrenai/jarvis/JarvisApplication;", "Landroid/app/Application;", "()V", "<set-?>", "Lcom/darrenai/jarvis/database/ReminderDatabase;", "db", "getDb", "()Lcom/darrenai/jarvis/database/ReminderDatabase;", "Lcom/darrenai/jarvis/database/ReminderDao;", "reminderDao", "getReminderDao", "()Lcom/darrenai/jarvis/database/ReminderDao;", "onCreate", "", "Companion", "app_debug"})
public final class JarvisApplication extends android.app.Application {
    private com.darrenai.jarvis.database.ReminderDatabase db;
    private com.darrenai.jarvis.database.ReminderDao reminderDao;
    private static com.darrenai.jarvis.JarvisApplication instance;
    @org.jetbrains.annotations.NotNull
    public static final com.darrenai.jarvis.JarvisApplication.Companion Companion = null;
    
    public JarvisApplication() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.darrenai.jarvis.database.ReminderDatabase getDb() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.darrenai.jarvis.database.ReminderDao getReminderDao() {
        return null;
    }
    
    @java.lang.Override
    public void onCreate() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u0086.\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\b"}, d2 = {"Lcom/darrenai/jarvis/JarvisApplication$Companion;", "", "()V", "<set-?>", "Lcom/darrenai/jarvis/JarvisApplication;", "instance", "getInstance", "()Lcom/darrenai/jarvis/JarvisApplication;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.darrenai.jarvis.JarvisApplication getInstance() {
            return null;
        }
    }
}