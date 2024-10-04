package com.example.myapplication.event

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import android.widget.Toast

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter.adapter_contact
import com.example.myapplication.databinding.ActivityEventForMamBinding
import com.example.myapplication.moduel.event_structure
import com.example.myapplication.moduel.event_viewmodel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class event_for_mam : AppCompatActivity() {
  lateinit var binding: ActivityEventForMamBinding
  private lateinit var viewModel: event_viewmodel
  private lateinit var adapter: adapter_contact


  override fun onCreate(savedInstanceState: Bundle?) {
    binding = ActivityEventForMamBinding.inflate(layoutInflater)
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(binding.root)
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets
    }


    val recyclerView = binding.recyclerView
    recyclerView.layoutManager = LinearLayoutManager(this)
    adapter = adapter_contact({ eventId -> showDeleteConfirmationDialog(eventId,true) }, true)
    recyclerView.adapter = adapter
    registerForContextMenu(recyclerView) // Register for context menu




    viewModel = ViewModelProvider(this)[event_viewmodel::class.java]
    viewModel.userlist.observe(this) { events ->
      adapter.update_event(events)
    }
    binding.floatingActionButton.setOnClickListener {
      val intent = Intent(this, add_data::class.java)
      startActivity(intent)
    }




  }
  override fun onCreateContextMenu(
    menu: ContextMenu?,
    v: View?,
    menuInfo: ContextMenu.ContextMenuInfo?
  ) {
    super.onCreateContextMenu(menu, v, menuInfo)
    menuInflater.inflate(R.menu.menu_event_item, menu)
  }

  private fun showDeleteConfirmationDialog(eventId: String, b: Boolean) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle("Delete Event")
    builder.setMessage("Are you sure you want to delete this event?")
    builder.setPositiveButton("Delete") { dialog, which ->
      deleteEvent(eventId)
    }
    builder.setNegativeButton("Cancel") { dialog, which ->
      dialog.dismiss()
    }
    val dialog: AlertDialog = builder.create()
    dialog.show()

  }

  private fun deleteEvent(eventId: String) {
    val database = FirebaseDatabase.getInstance()
    val eventRef = database.getReference("Event").child(eventId)

    // Get the image URL before deleting the event
    eventRef.addListenerForSingleValueEvent(object : ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
        if (snapshot.exists()) {
          val event = snapshot.getValue(event_structure::class.java)
          val imageUrl = event?.image

          // Delete the event from the database
          eventRef.removeValue()
            .addOnSuccessListener {
              Toast.makeText(this@event_for_mam, "Event deleted", Toast.LENGTH_SHORT).show()
              viewModel.fetchEvents()

              // Delete the image from storage if it exists
              if (imageUrl != null) {
                val storageRef = FirebaseStorage.getInstance().reference
                val imageRef = storageRef.child("images/$eventId") // Assuming your image path is "images/$eventId"
                imageRef.delete()
                  .addOnSuccessListener {
                    // Image deleted successfully
                  }
                  .addOnFailureListener {
                    // Failed to delete image
                  }
              }
            }
            .addOnFailureListener {
              Toast.makeText(this@event_for_mam, "Failed to delete event", Toast.LENGTH_SHORT).show()
            }
        }
      }

      override fun onCancelled(error: DatabaseError) {
        Toast.makeText(this@event_for_mam, "Failed to fetch event data", Toast.LENGTH_SHORT).show()
      }
    })
  }


}




