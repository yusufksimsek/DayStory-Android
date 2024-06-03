package com.example.daystory.viewmodel

import android.app.Application
import android.content.Context
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daystory.api.model.UserLogin
import com.example.daystory.api.service.RetrofitClient
import kotlinx.coroutines.launch

class LoginViewModel(private val application: Application): AndroidViewModel(application) {

    private val _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?> = _emailError

    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> = _passwordError

    private val _loginResult = MutableLiveData<Result<String>>()
    val loginResult: LiveData<Result<String>> = _loginResult

    fun loginValidateFields(email: String, password: String): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            _emailError.value = "Email alanı boş bırakılamaz"
            isValid = false
        }else if (email.length < 3 ) {
            _emailError.value = "Email en az 3 karakter olmalıdır"
            isValid = false
        }else if (email.length > 50) {
            _emailError.value = "Email en fazla 50 karakter olmalıdır"
            isValid = false
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailError.value = "Geçerli bir E-Mail adresi giriniz"
            isValid = false
        }else {
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

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.login(UserLogin(email, password))
                if (response.isSuccessful) {
                    val token = response.body()?.token ?: ""
                    saveToken(token)
                    _loginResult.value = Result.success(token)
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = when (response.code()) {
                        404 -> "Kullanıcı bulunamadı"
                        401 -> "Yanlış şifre"
                        500 -> "Sunucu hatası: $errorBody"
                        else -> "Bilinmeyen hata: $errorBody"
                    }
                    _loginResult.value = Result.failure(Exception(errorMessage))
                }
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            }
        }
    }

    private fun saveToken(token: String) {
        val sharedPreferences = application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("auth_token", token).apply()
    }
}