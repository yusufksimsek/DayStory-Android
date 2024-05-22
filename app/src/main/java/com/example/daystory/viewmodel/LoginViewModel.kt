package com.example.daystory.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {

    private val _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?> = _emailError

    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> = _passwordError

    fun loginValidateFields(email: String, password: String): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            _emailError.value = "Email alanı boş bırakılamaz"
            isValid = false
        } else {
            _emailError.value = null
        }

        if (password.isEmpty()) {
            _passwordError.value = "Şifre alanı boş bırakılamaz"
            isValid = false
        } else {
            _passwordError.value = null
        }

        return isValid
    }

}