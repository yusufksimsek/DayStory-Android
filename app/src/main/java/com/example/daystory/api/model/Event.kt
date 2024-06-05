package com.example.daystory.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    val id: Int? = null,
    val title: String,
    val description: String,
    val date: String,
    val time: String? = null,
    val priority: String? = null
) : Parcelable
