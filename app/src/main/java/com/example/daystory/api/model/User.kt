package com.example.daystory.api.model

data class UserRegister(
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val password: String,
    val passwordConfirmed: String,
    val birthDate: String,
    val gender: String
)

data class UserLogin(
    val email: String,
    val password: String
)

data class UserRegisterResponse(
    val statusCode: Int?,
    val message: String?,
)

data class Token(
    val token: String
)

data class UserLoginResponse(
    val data: Token
)

data class UserGetResponse(
    val statusCode: Int,
    val message: String,
    val data: UserProfile
)

data class UserProfile(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val birthDate: String,
    val gender: String
)