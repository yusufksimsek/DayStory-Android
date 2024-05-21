package com.example.daystory.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.daystory.model.Event

@Database(entities = [ Event::class ], version = 1)
abstract class EventDatabase: RoomDatabase() {
    abstract fun getEventDao(): EventDao

    companion object{
        @Volatile
        private var instance: EventDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?:
        synchronized(LOCK){
            instance ?:
            createDatabase(context).also{
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                EventDatabase::class.java,
                "event_db"
            ).build()
    }
}