package com.example.daystory.repository

import com.example.daystory.api.model.Event
import com.example.daystory.api.service.EventService
import com.example.daystory.api.service.RetrofitClient
import retrofit2.Response

class EventRepository {
    suspend fun createEvent(event: Event): Response<EventService.crudResponse> = RetrofitClient.eventApi.createEvent(event)

    suspend fun getEventsByDate(date: String): Response<EventService.BaseResponse<List<Event>>> {
        return RetrofitClient.eventApi.getEventsByDate(date)
    }

    suspend fun deleteEvent(eventId: Int): Response<EventService.BaseResponse<String>> {
        return RetrofitClient.eventApi.deleteEvent(eventId)
    }

    suspend fun updateEvent(event: Event): Response<EventService.BaseResponse<String>> {
        return RetrofitClient.eventApi.updateEvent(event)
    }

}