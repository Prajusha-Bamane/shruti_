package com.example.myapplication.moduel

import android.os.Parcel
import android.os.Parcelable

data class event_structure(
    val date: String? = null,
    val description: String? = null,
    val title: String? = null,
    var image: String? = null,
    val id: String? = null
)