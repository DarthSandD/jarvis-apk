package com.darrenai.jarvis.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.darrenai.jarvis.database.ReminderEntity
import com.darrenai.jarvis.database.ReminderDao
import android.content.Context

@Database(
    entities = [ReminderEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ReminderDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile
        private var INSTANCE: ReminderDatabase? = null

        fun getDatabase(context: Context): ReminderDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ReminderDatabase::class.java,
                    "jarvis_reminders.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
