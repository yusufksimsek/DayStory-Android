package com.example.daystory.repository

import com.example.daystory.database.EventDatabase
import com.example.daystory.model.Event

class EventRepository(private val db: EventDatabase) {

    suspend fun insertEvent(event: Event) = db.getEventDao().insertEvent(event)

    suspend fun deleteEvent(event: Event) = db.getEventDao().deleteEvent(event)

    suspend fun updateEvent(event: Event) = db.getEventDao().updateEvent(event)

    fun getAllEvents() = db.getEventDao().getAllEvents()

    fun searchEvent(query: String?) = db.getEventDao().searchEvent(query)

}