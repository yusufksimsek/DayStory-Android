package com.example.daystory.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "events")
@Parcelize
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val eventTitle: String,
    val eventDesc: String,
    val eventDate: String
) : Parcelable
