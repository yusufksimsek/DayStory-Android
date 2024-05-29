package com.example.daystory.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.daystory.model.Event

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    @Update
    suspend fun updateEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)

    @Query("SELECT * FROM EVENTS ORDER BY id Desc")
    fun getAllEvents(): LiveData<List<Event>>

    @Query("SELECT * FROM events WHERE eventDate = :date")
    fun getEventsByDate(date: String): LiveData<List<Event>>

}