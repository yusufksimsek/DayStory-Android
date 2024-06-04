package com.example.daystory.UI.viewmodel

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

    private val _titleError = MutableLiveData<String?>()
    val addTitleError: LiveData<String?> = _titleError

    private val _descError = MutableLiveData<String?>()
    val addDescError: LiveData<String?> = _descError

    fun validateTitle(title: String) {
        _titleError.value = if (title.length > 250) "Başlık en fazla 250 karakter olabilir" else null
    }

    fun validateDesc(desc: String) {
        _descError.value = if (desc.length > 350) "Açıklama en fazla 350 karakter olabilir" else null
    }

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