package com.example.daystory.api.service

import com.example.daystory.api.model.Event
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface EventService {
    @POST("api/Events/")
    suspend fun createEvent(@Body event: Event): Response<String>
}