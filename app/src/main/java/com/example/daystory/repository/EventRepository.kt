package com.example.daystory.repository

import com.example.daystory.api.model.Event
import com.example.daystory.api.service.RetrofitClient
import retrofit2.Response

class EventRepository {
    suspend fun createEvent(event: Event) = RetrofitClient.eventApi.createEvent(event)

    suspend fun getEventsByDate(date: String): Response<List<Event>> {
        return RetrofitClient.eventApi.getEventsByDate(date)
    }

    suspend fun getAllEvents(): Response<List<Event>> {
        return RetrofitClient.eventApi.getAllEvents()
    }

}