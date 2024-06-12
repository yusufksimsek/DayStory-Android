package com.example.daystory.api.service

import android.annotation.SuppressLint
import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@SuppressLint("StaticFieldLeak")
object RetrofitClient {
    private lateinit var context: Context

    fun initialize(context: Context) {
        this.context = context
    }

    private const val BASE_URL = "https://talent.mobven.com:5043/"
    //private const val BASE_URL = "https://talent.mobven.com:6003/"
    //private const val BASE_URL = "http://165.22.93.225:5030/"


    val retrofit: Retrofit by lazy{

          val loggingInterceptor = HttpLoggingInterceptor().apply {
             setLevel(HttpLoggingInterceptor.Level.BODY)
         }

          val client: OkHttpClient = OkHttpClient.Builder()
             .readTimeout(60,TimeUnit.SECONDS)
             .connectTimeout(60,TimeUnit.SECONDS)
             .addInterceptor(loggingInterceptor)
             .addInterceptor(AuthInterceptor(context))
             .addInterceptor(ChuckerInterceptor(context))
             .build()

         
         Retrofit.Builder()
             .baseUrl(BASE_URL)
             .client(client)
             .addConverterFactory(GsonConverterFactory.create())
             .build()
     }

    val userApi: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

    val eventApi: EventService by lazy {
        retrofit.create(EventService::class.java)
    }

}
