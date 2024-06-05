package com.example.daystory.api.service

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", "") ?: ""
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .addHeader("Authorization", "Bearer $token")
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
