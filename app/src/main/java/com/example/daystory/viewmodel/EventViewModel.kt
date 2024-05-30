package com.example.daystory.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.daystory.model.Event
import com.example.daystory.repository.EventRepository
import kotlinx.coroutines.launch

class EventViewModel(app: Application, private val eventRepository: EventRepository) : AndroidViewModel(app) {

    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String> get() = _selectedDate

    fun setSelectedDate(date: String) {
        _selectedDate.value = date
    }

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

    //val getEvents = eventRepository.getAllEvents()

    fun getEventsByDate(date: String): LiveData<List<Event>> {
        return eventRepository.getEventsByDate(date)
    }

}