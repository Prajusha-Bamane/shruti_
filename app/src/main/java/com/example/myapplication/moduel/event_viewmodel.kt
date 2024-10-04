package com.example.myapplication.moduel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.repo.event_repo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class event_viewmodel: ViewModel() {
  private val _userlist = MutableLiveData<List<event_structure>>()
  val userlist: LiveData<List<event_structure>> = _userlist

  init {
    fetchEvents()
  }

  fun fetchEvents() {
    val eventsReference = FirebaseDatabase.getInstance().getReference("Event")
    eventsReference.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
        val events = mutableListOf<event_structure>()
        for (eventSnapshot in snapshot.children) {
          val event = eventSnapshot.getValue(event_structure::class.java)
          if (event != null) {
            events.add(event)
          }
        }
        _userlist.value = events
      }

      override fun onCancelled(error: DatabaseError) {
        // Handle error
      }
    })
  }
}

