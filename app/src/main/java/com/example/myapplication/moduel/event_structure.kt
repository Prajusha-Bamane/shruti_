package com.example.myapplication.moduel

import android.os.Parcel
import android.os.Parcelable

data class event_structure(
    val date: String? = null,val description: String? = null,
    val title: String? = null,
    var image: String? = null,
    val id: String? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeString(description)
        parcel.writeString(title)
        parcel.writeString(image)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<event_structure> {
        override fun createFromParcel(parcel: Parcel): event_structure {
            return event_structure(parcel)
        }

        override fun newArray(size: Int): Array<event_structure?> {
            return arrayOfNulls(size)
        }
    }
}