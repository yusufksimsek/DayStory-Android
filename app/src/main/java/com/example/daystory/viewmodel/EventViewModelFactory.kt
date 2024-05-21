package com.example.daystory.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.daystory.repository.EventRepository

class EventViewModelFactory(
    val app:Application,
    private val eventRepository: EventRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EventViewModel(app,eventRepository) as T
    }

}