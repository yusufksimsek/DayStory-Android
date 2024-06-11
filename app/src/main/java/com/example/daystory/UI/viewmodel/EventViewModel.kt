package com.example.daystory.UI.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.daystory.api.model.Event
import com.example.daystory.repository.EventRepository
import kotlinx.coroutines.launch

class EventViewModel(app: Application, private val eventRepository: EventRepository) : AndroidViewModel(app) {

    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String> get() = _selectedDate

    private val _titleError = MutableLiveData<String?>()
    val addTitleError: LiveData<String?> = _titleError

    private val _descError = MutableLiveData<String?>()
    val addDescError: LiveData<String?> = _descError

    private val _eventCreationStatus = MutableLiveData<String?>()
    val eventCreationStatus: LiveData<String?> = _eventCreationStatus

    private val _allEvents = MutableLiveData<List<Event>?>()
    val allEvents: LiveData<List<Event>?> get() = _allEvents

    private val _eventsByDate = MutableLiveData<List<Event>?>()
    val eventsByDate: LiveData<List<Event>?> get() = _eventsByDate

    private val _eventDeletionStatus = MutableLiveData<String?>()
    val eventDeletionStatus: LiveData<String?> = _eventDeletionStatus

    private val _eventUpdateStatus = MutableLiveData<String?>()
    val eventUpdateStatus: LiveData<String?> get() = _eventUpdateStatus

    fun validateTitle(title: String) {
        _titleError.value = when {
            title.isEmpty() -> "Başlık boş olamaz"
            title.length > 250 -> "Başlık en fazla 250 karakter olabilir"
            else -> null
        }
    }

    fun validateDesc(desc: String) {
        _descError.value = when {
            desc.length < 3 -> "Açıklama 3 karakterden az olamaz"
            desc.length > 350 -> "Açıklama en fazla 350 karakter olabilir"
            else -> null
        }
    }

    fun setSelectedDate(date: String) {
        _selectedDate.value = date
        fetchEventsByDate(date)
    }

    fun fetchEventsByDate(date: String) = viewModelScope.launch {
        try {
            val response = eventRepository.getEventsByDate(date)
            if (response.isSuccessful) {
                _eventsByDate.postValue(response.body()?.data)
            } else {
                _eventsByDate.postValue(emptyList())
            }
        } catch (e: Exception) {
            _eventsByDate.postValue(emptyList())
        }
    }

    fun deleteEvent(eventId: Int) = viewModelScope.launch {
        try {
            val response = eventRepository.deleteEvent(eventId)
            if (response.isSuccessful) {
                _eventDeletionStatus.postValue("Başarıyla silindi")
                fetchEventsByDate(_selectedDate.value ?: "")
            } else {
                _eventDeletionStatus.postValue("Failed to delete event: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            _eventDeletionStatus.postValue("Network error: ${e.message}")
        }
    }

    fun clearEventDeletionStatus() {
        _eventDeletionStatus.value = null
    }

    fun addEvent(event: Event) = viewModelScope.launch {
        try {
            val response = eventRepository.createEvent(event)
            if (response.isSuccessful) {
                _eventCreationStatus.postValue("Event başarıyla oluşturuldu")
            } else {
                _eventCreationStatus.postValue("Failed to create event: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            _eventCreationStatus.postValue("Network error: ${e.message}")
        }
    }

    fun clearEventCreationStatus() {
        _eventCreationStatus.value = null
    }

    fun updateEvent(event: Event) = viewModelScope.launch {
        try {
            val eventWithoutPriority = event.copy(priority = null)
            val response = eventRepository.updateEvent(eventWithoutPriority)
            if (response.isSuccessful) {
                _eventUpdateStatus.postValue("Event başarıyla güncellendi")
                fetchEventsByDate(_selectedDate.value ?: "")
            } else {
                _eventUpdateStatus.postValue("Failed to update event: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            _eventUpdateStatus.postValue("Network error: ${e.message}")
        }
    }

    fun clearEventUpdateStatus() {
        _eventUpdateStatus.value = null
    }

}