package com.example.daystory.api.service


import com.example.daystory.api.model.UserRegister
import com.example.daystory.api.model.UserRegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("api/Users/register")
    fun registerUser(@Body user: UserRegister): Call<UserRegisterResponse>

}