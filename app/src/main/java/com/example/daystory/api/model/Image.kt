package com.example.daystory.api.model

data class imageResponseModel(
    val status: Int,
    val message: String,
    val data: Data
)

data class Data(
    val id: Int,
    val date: String,
    val imagePath: String
)

data class daySummaryResponse(
    val statusCode: Int,
    val message: String
)
