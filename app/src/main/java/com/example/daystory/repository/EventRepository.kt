package com.example.daystory.repository

import com.example.daystory.api.model.Event
import com.example.daystory.api.service.RetrofitClient

class EventRepository {
    suspend fun createEvent(event: Event) = RetrofitClient.eventApi.createEvent(event)

}