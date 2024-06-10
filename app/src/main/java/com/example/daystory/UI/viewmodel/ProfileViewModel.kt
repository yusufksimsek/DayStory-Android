package com.example.daystory.UI.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.daystory.api.model.UserProfile
import com.example.daystory.api.service.RetrofitClient
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val _userDetails = MutableLiveData<UserProfile>()
    val userDetails: LiveData<UserProfile> = _userDetails

    fun fetchUserDetails() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.userApi.getUserDetails()
                if (response.isSuccessful) {
                    _userDetails.value = response.body()?.data
                } else {
                    // Handle error case
                }
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }

}