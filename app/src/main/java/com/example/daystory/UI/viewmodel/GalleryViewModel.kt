package com.example.daystory.UI.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daystory.api.service.EventService
import com.example.daystory.api.service.RetrofitClient
import kotlinx.coroutines.launch

class GalleryViewModel : ViewModel() {
    private val _daySummaries = MutableLiveData<List<EventService.DaySummary>>()
    val daySummaries: LiveData<List<EventService.DaySummary>> get() = _daySummaries

    fun fetchDaySummaries() {
        viewModelScope.launch {
            val response = RetrofitClient.eventApi.getAllDaySummaries()
            if (response.isSuccessful) {
                response.body()?.data?.let { daySummaries ->
                    _daySummaries.value = daySummaries
                }
            } else {
                // Hata durumunu yönet
            }
        }
    }
}
