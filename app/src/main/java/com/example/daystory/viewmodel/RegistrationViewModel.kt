package com.example.daystory.viewmodel

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Locale

class RegistrationViewModel : ViewModel() {

    private val _nameError = MutableLiveData<String?>()
    val nameError: LiveData<String?> = _nameError

    private val _surnameError = MutableLiveData<String?>()
    val surnameError: LiveData<String?> = _surnameError

    private val _genderError = MutableLiveData<String?>()
    val genderError: LiveData<String?> = _genderError

    private val _dateError = MutableLiveData<String?>()
    val dateError: LiveData<String?> = _dateError

    private val _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?> = _emailError

    private val _usernameError = MutableLiveData<String?>()
    val usernameError: LiveData<String?> = _usernameError

    private val _password1Error = MutableLiveData<String?>()
    val password1Error: LiveData<String?> = _password1Error

    private val _password2Error = MutableLiveData<String?>()
    val password2Error: LiveData<String?> = _password2Error

    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String> = _selectedDate

    fun firstvalidateFields(name:String,surname:String,gender:String,date:String): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            _nameError.value = "Bu alan boş bırakılamaz"
            isValid = false
        } else {
            _nameError.value = null
        }

        if (surname.isEmpty()) {
            _surnameError.value = "Bu alan boş bırakılamaz"
            isValid = false
        } else {
            _surnameError.value = null
        }

        if (gender.isEmpty()) {
            _genderError.value = "Bu alan boş bırakılamaz"
            isValid = false
        } else {
            _genderError.value = null
        }

        if (date.isEmpty()) {
            _dateError.value = "Bu alan boş bırakılamaz"
            isValid = false
        } else {
            _dateError.value = null
        }

        return isValid
    }

    fun secondvalidateFields(email:String,username:String,password1:String,password2:String): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            _emailError.value = "Bu alan boş bırakılamaz"
            isValid = false
        } else {
            _emailError.value = null
        }

        if (username.isEmpty()) {
            _usernameError.value = "Bu alan boş bırakılamaz"
            isValid = false
        } else {
            _usernameError.value = null
        }

        if (password1.isEmpty()) {
            _password1Error.value = "Bu alan boş bırakılamaz"
            isValid = false
        } else {
            _password1Error.value = null
        }

        if (password2.isEmpty()) {
            _password2Error.value = "Bu alan boş bırakılamaz"
            isValid = false
        } else {
            _password2Error.value = null
        }

        if (password1.isNotEmpty() && password2.isNotEmpty()) {
            if (password1 != password2) {
                _password2Error.value = "Şifreler aynı değil!"
                isValid = false
            } else if (password1 == password2) {
                _password2Error.value = null
            }
        }

        return isValid
    }

    fun validateDate(selectedDate: Long, today: Long) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateString = dateFormat.format(selectedDate)

        if (selectedDate > today) {
            _dateError.value = "Gelecek bir tarih seçemezsiniz."
        } else {
            _dateError.value = null
            _selectedDate.value = dateString
        }
    }
}