package com.example.daystory.UI.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daystory.api.model.Event
import com.example.daystory.api.service.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GalleryDetailViewModel : ViewModel() {

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> get() = _events

    private val retrofitClient = RetrofitClient

    fun fetchEvents(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = retrofitClient.eventApi.getEventsByDate(date)
                if (response.isSuccessful && response.body()?.data != null) {
                    withContext(Dispatchers.Main) {
                        _events.value = response.body()?.data ?: emptyList()
                    }
                } else {
                    // Hata durumlarını yönet
                }
            } catch (e: Exception) {
                // Hata durumlarını yönet
            }
        }
    }
}
