package com.example.daystory.UI.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.daystory.api.service.RetrofitClient
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ImageDetailViewModel : ViewModel() {

    fun getDaySummary() = liveData(Dispatchers.IO) {
        val currentDate = SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(Date())
        try {
            val response = RetrofitClient.eventApi.getDaySummary(currentDate)
            if (response.isSuccessful) {
                emit(response.body()?.data)
            } else {
                emit(null)
            }
        } catch (e: Exception) {
            emit(null)
        }
    }
}
