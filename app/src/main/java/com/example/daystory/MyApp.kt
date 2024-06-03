package com.example.daystory

import android.app.Application
import com.example.daystory.api.service.RetrofitClient

class MyApp : Application(){

    override fun onCreate() {
        super.onCreate()
        RetrofitClient.initialize(this)
    }

}