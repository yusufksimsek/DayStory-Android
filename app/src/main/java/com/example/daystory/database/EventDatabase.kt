package com.example.daystory.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.daystory.model.Event

@Database(entities = [Event::class], version = 2)
abstract class EventDatabase: RoomDatabase() {
    abstract fun getEventDao(): EventDao

    companion object{
        @Volatile
        private var instance: EventDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                EventDatabase::class.java,
                "event_db"
            )
                .addMigrations(MIGRATION_1_2)
                .build()

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE events ADD COLUMN eventDate TEXT NOT NULL DEFAULT ''")
            }
        }
    }
}
