package com.example.myapplication.event

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityUpdateEventBinding
import com.example.myapplication.moduel.event_structure
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import android.app.Activity
import android.view.View

class Update_event : AppCompatActivity() {
  lateinit var binding: ActivityUpdateEventBinding
  private var selectedImageUri: Uri? = null
  private lateinit var storageRef: StorageReference
  private lateinit var eventId: String

  private val resultLauncher =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
      if (result.resultCode == Activity.RESULT_OK) {
        val data: Intent? = result.data
        selectedImageUri = data?.data
        binding.imageView.setImageURI(selectedImageUri)
      }
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    binding = ActivityUpdateEventBinding.inflate(layoutInflater)
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(binding.root)
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets
    }

    eventId = intent.getStringExtra("eventId") ?: ""


    storageRef = FirebaseStorage.getInstance().reference

    if (eventId.isNotEmpty()) {
      val database = FirebaseDatabase.getInstance()
      val eventRef = database.getReference("Event").child(eventId)

      eventRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
          if (snapshot.exists()) {
            val event = snapshot.getValue(event_structure::class.java)
            // Populate UI with event data
            binding.editTextTitle.setText(event?.title)
            binding.editTextDescription.setText(event?.description)
            binding.editTextDate.setText(event?.date)
            if (event?.image != null) {
              Glide.with(this@Update_event)
                .load(event.image)
                .into(binding.imageView)
            }
          }
        }

        override fun onCancelled(error: DatabaseError) {
          // Handle errors
          Toast.makeText(
            this@Update_event,
            "Failed to fetch event data",
            Toast.LENGTH_SHORT
          ).show()
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
    progressBar.visibility = android.view.View.VISIBLE

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
          Toast.makeText(
            this@Update_event,
            "Failed to upload image",
            Toast.LENGTH_SHORT
          ).show()
          progressBar.visibility = View.GONE
        }
      }
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
            Toast.makeText(this@Update_event, "Event not found", Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE
          }
        }

        override fun onCancelled(error: DatabaseError) {
          // Handle errors
          Toast.makeText(this@Update_event, "Failed to fetch event data", Toast.LENGTH_SHORT).show()
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
        Toast.makeText(this@Update_event, "Event updated", Toast.LENGTH_SHORT).show()
        finish()
        progressBar.visibility = View.GONE

      }
      .addOnFailureListener {
        Toast.makeText(this@Update_event, "Failed to update event", Toast.LENGTH_SHORT).show()
        progressBar.visibility = View.GONE
      }
  }
}