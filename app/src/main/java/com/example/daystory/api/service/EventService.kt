package com.example.daystory.api.service

import com.example.daystory.api.model.Event
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface EventService {
    @POST("api/Events/")
    suspend fun createEvent(@Body event: Event): Response<String>

    @GET("api/Events/day")
    suspend fun getEventsByDate(@Query("date") date: String): Response<BaseResponse<List<Event>>>

    @DELETE("api/Events/{id}")
    suspend fun deleteEvent(@Path("id") eventId: Int): Response<BaseResponse<String>>

    @PUT("api/Events/")
    suspend fun updateEvent(@Body event: Event): Response<BaseResponse<String>>

    data class BaseResponse<T>(
        val statusCode: Int?,
        val message: String?,
        val data: T?
    )
}