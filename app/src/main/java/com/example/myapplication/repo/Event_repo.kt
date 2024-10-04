package com.example.myapplication.repo

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.moduel.event_structure
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class event_repo {

  private val database = FirebaseDatabase.getInstance()
  private val eventRef = database.getReference("Event")

  companion object {
    private var instance: event_repo? = null
    fun getInstance(): event_repo {
      if (instance == null) {
        instance = event_repo()
      }
      return instance!!
    }
  }

  fun addevent(eventList: MutableLiveData<List<event_structure>>) {
    eventRef.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
        val events = mutableListOf<event_structure>()
        for (childSnapshot in snapshot.children) {
          val event = childSnapshot.getValue(event_structure::class.java)
          event?.let { events.add(it) }
        }
        eventList.postValue(events)
      }

      override fun onCancelled(error: DatabaseError) {
        // Handle error
      }
    })
  }
}