package com.example.daystory.api.service


import com.example.daystory.api.model.UserGetResponse
import com.example.daystory.api.model.UserLogin
import com.example.daystory.api.model.UserLoginResponse
import com.example.daystory.api.model.UserProfile
import com.example.daystory.api.model.UserRegister
import com.example.daystory.api.model.UserRegisterResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {
    @POST("api/Users/register")
    fun registerUser(@Body user: UserRegister): Call<UserRegisterResponse>

    @POST("api/Users/login")
    suspend fun login(@Body loginUser: UserLogin): Response<UserLoginResponse>

    @GET("api/Users")
    suspend fun getUserDetails(): Response<UserGetResponse>

}