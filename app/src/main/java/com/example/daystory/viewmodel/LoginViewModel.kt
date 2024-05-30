package com.example.daystory.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {

    private val _usernameError = MutableLiveData<String?>()
    val usernameError: LiveData<String?> = _usernameError

    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> = _passwordError

    fun loginValidateFields(username: String, password: String): Boolean {
        var isValid = true

        if (username.isEmpty()) {
            _usernameError.value = "Kullanıcı Adı alanı boş bırakılamaz"
            isValid = false
        }else if (username.length < 3 ) {
            _usernameError.value = "Kullanıcı adı en az 3 karakter olmalıdır"
            isValid = false
        }else if (username.length > 50) {
            _usernameError.value = "Kullanıcı adı en fazla 50 karakter olmalıdır"
            isValid = false
        }else {
            _usernameError.value = null
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