package com.darrenai.jarvis

import android.app.Application
import androidx.room.Room
import com.darrenai.jarvis.database.ReminderDatabase
import com.darrenai.jarvis.database.ReminderDao

class JarvisApplication : Application() {

    lateinit var db: ReminderDatabase
        private set

    lateinit var reminderDao: ReminderDao
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        db = ReminderDatabase.getDatabase(applicationContext)
        reminderDao = db.reminderDao()
    }

    companion object {
        lateinit var instance: JarvisApplication
            private set
    }
}
