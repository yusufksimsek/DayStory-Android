package com.example.daystory.api.model

data class UserRegister(
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val password: String,
    val passwordConfirmed: String,
    val birthDate: String,
    val gender: String)

data class UserRegisterResponse(
    val message: String
)
