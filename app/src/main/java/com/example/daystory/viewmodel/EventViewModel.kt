package com.example.daystory.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.daystory.model.Event
import com.example.daystory.repository.EventRepository
import kotlinx.coroutines.launch

class EventViewModel(app: Application, private val eventRepository: EventRepository) : AndroidViewModel(app) {

    fun addEvent(event: Event) =
        viewModelScope.launch {
            eventRepository.insertEvent(event)
        }

    fun deleteEvent(event: Event) =
        viewModelScope.launch {
            eventRepository.deleteEvent(event)
        }

    fun updateEvent(event: Event) =
        viewModelScope.launch {
            eventRepository.updateEvent(event)
        }

    val getEvents = eventRepository.getAllEvents()

    fun searchEvent(query: String?) =
        eventRepository.searchEvent(query)

}