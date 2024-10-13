package com.example.myapplication.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter.adapter_contact
import com.example.myapplication.databinding.FragmentEventMamBinding
import com.example.myapplication.moduel.event_structure
import com.example.myapplication.moduel.event_viewmodel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class event_mam : Fragment() {

  private lateinit var binding: FragmentEventMamBinding
  private lateinit var viewModel: event_viewmodel
  private lateinit var adapter: adapter_contact

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentEventMamBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel = ViewModelProvider(this)[event_viewmodel::class.java]

    binding.recyclerView.layoutManager = LinearLayoutManager(context)
    adapter = adapter_contact({ eventId -> showDeleteConfirmationDialog(eventId) }, true)
    binding.recyclerView.adapter = adapter

    viewModel.userlist.observe(viewLifecycleOwner) { events ->
      adapter.update_event(events)
      binding.progressBar.visibility = View.GONE
    }

    viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
      if (isLoading) {
        binding.progressBar.visibility = View.VISIBLE
      }
    }


    binding.floatingActionButton.setOnClickListener {
      val fragmentManager = (context as FragmentActivity).supportFragmentManager
      val fragmentTransaction = fragmentManager.beginTransaction()
      fragmentTransaction.replace(R.id.relative, add_data_fragment()) // Replace with your container ID
      fragmentTransaction.addToBackStack(null)
      fragmentTransaction.commit()
    }
  }


  private fun showDeleteConfirmationDialog(eventId: String) {
    val builder = AlertDialog.Builder(requireContext())
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

    eventRef.addListenerForSingleValueEvent(object : ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
        if (snapshot.exists()) {
          val event = snapshot.getValue(event_structure::class.java)
          val imageUrl = event?.image

          eventRef.removeValue()
            .addOnSuccessListener {
              Toast.makeText(context, "Event deleted", Toast.LENGTH_SHORT).show()
              viewModel.fetchEvents()

              if (imageUrl != null) {
                val storageRef = FirebaseStorage.getInstance().reference
                val imageRef = storageRef.child("images/$eventId")
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
              Toast.makeText(context, "Failed to delete event", Toast.LENGTH_SHORT).show()
            }
        }
      }

      override fun onCancelled(error: DatabaseError) {
        Toast.makeText(context, "Failed to fetch event data", Toast.LENGTH_SHORT).show()
      }
    })
  }
}