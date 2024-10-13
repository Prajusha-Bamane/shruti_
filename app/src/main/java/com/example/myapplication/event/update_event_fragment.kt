package com.example.myapplication.event

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.FragmentUpdateEventFragmentBinding
import com.example.myapplication.moduel.event_structure
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class update_event_fragment : Fragment() {

  private lateinit var binding: FragmentUpdateEventFragmentBinding
  private var selectedImageUri: Uri? = null
  private lateinit var storageRef:StorageReference
  private lateinit var eventId: String

  private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    if (result.resultCode == Activity.RESULT_OK) {
      val data: Intent? = result.data
      selectedImageUri = data?.data
      binding.imageView.setImageURI(selectedImageUri)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentUpdateEventFragmentBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    eventId = arguments?.getString("eventId") ?: ""
    storageRef = FirebaseStorage.getInstance().reference

    if (eventId.isNotEmpty()) {
      val database = FirebaseDatabase.getInstance()
      val eventRef = database.getReference("Event").child(eventId)

      eventRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {if (snapshot.exists()) {
          val event = snapshot.getValue(event_structure::class.java)
          binding.editTextTitle.setText(event?.title)
          binding.editTextDescription.setText(event?.description)
          binding.editTextDate.setText(event?.date)
          if (event?.image != null) {
            Glide.with(requireContext())
              .load(event.image)
              .into(binding.imageView)
          }
        }
        }

        override fun onCancelled(error: DatabaseError) {
          Toast.makeText(context, "Failed to fetch event data", Toast.LENGTH_SHORT).show()
        }
      })

      binding.buttonUpdate.setOnClickListener {
        updateEventData()
      }

      binding.imageView.setOnClickListener {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
      }
    }
  }

  private fun updateEventData() {
    val updatedTitle = binding.editTextTitle.text.toString()
    val updatedDescription = binding.editTextDescription.text.toString()
    val updatedDate = binding.editTextDate.text.toString()
    val progressBar = binding.progressBar
    progressBar.visibility = View.VISIBLE

    if (selectedImageUri != null) {
      val imageRef = storageRef.child("images/$eventId")
      val uploadTask = imageRef.putFile(selectedImageUri!!)

      uploadTask.addOnProgressListener { taskSnapshot ->
        val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
        progressBar.progress = progress.toInt()
      }

      uploadTask.continueWithTask { task ->
        if (!task.isSuccessful) {
          task.exception?.let {
            throw it
          }
        }
        imageRef.downloadUrl
      }.addOnCompleteListener { task ->
        if (task.isSuccessful) {
          val downloadUri = task.result
          updateEventInDatabase(eventId, updatedTitle, updatedDescription, updatedDate, downloadUri)
        } else {
          Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
          progressBar.visibility = View.GONE
        }}
    } else {
      val database = FirebaseDatabase.getInstance()
      val eventRef = database.getReference("Event").child(eventId)

      eventRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
          if (snapshot.exists()) {
            val event = snapshot.getValue(event_structure::class.java)
            val currentImageUrl = event?.image

            updateEventInDatabase(
              eventId,
              updatedTitle,
              updatedDescription,
              updatedDate,
              currentImageUrl?.let { Uri.parse(it) })
          } else {
            Toast.makeText(context, "Event not found", Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE
          }
        }

        override fun onCancelled(error: DatabaseError) {
          Toast.makeText(context, "Failed to fetch event data", Toast.LENGTH_SHORT).show()
          progressBar.visibility = View.GONE
        }
      })
    }
  }

  private fun updateEventInDatabase(
    eventId: String,
    title: String,
    description: String,
    date: String,
    imageUrl: Uri?
  ) {
    val progressBar = binding.progressBar
    val database = FirebaseDatabase.getInstance()
    val eventRef = database.getReference("Event").child(eventId)

    val updatedEvent = event_structure(date, description, title, imageUrl?.toString(), eventId)
    eventRef.setValue(updatedEvent)
      .addOnSuccessListener {
        Toast.makeText(context, "Event updated", Toast.LENGTH_SHORT).show()
        // Handle navigation or other actions after successful update
        progressBar.visibility = View.GONE
      }
      .addOnFailureListener {
        Toast.makeText(context, "Failed to update event", Toast.LENGTH_SHORT).show()
        progressBar.visibility = View.GONE
      }
  }
}