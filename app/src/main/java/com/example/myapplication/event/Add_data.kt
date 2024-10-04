package com.example.myapplication.event

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R.id.main
import com.example.myapplication.databinding.ActivityAddDataBinding
import com.example.myapplication.moduel.event_structure
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class add_data : AppCompatActivity() {

  lateinit var binding: ActivityAddDataBinding
  private var eventref = FirebaseDatabase.getInstance().getReference().child("Event")
  private var selectedImageUri: Uri? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    binding = ActivityAddDataBinding.inflate(layoutInflater)
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(binding.root)

    ViewCompat.setOnApplyWindowInsetsListener(findViewById(main)) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets
    }

    val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
      binding.imageView.setImageURI(uri)
      selectedImageUri = uri
    }
    binding.imageView.setOnClickListener {
      pickImage.launch("image/*")
    }

    binding.buttonAdd.setOnClickListener {
      insertdata()
    }
  }

  private fun insertdata() {
    val title = binding.editTextTitle.text.toString()
    val description = binding.editTextDescription.text.toString()
    val date = binding.editTextDate.text.toString()



    if (title.isBlank() || description.isBlank() || date.isBlank()) {
      Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
      return
    }
    binding.buttonAdd.isEnabled = false
    binding.progressBar.visibility = View.VISIBLE


    if (selectedImageUri == null) {
      Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
      return
    }

    val newEventRef = eventref.push()
    val eventId = newEventRef.key


    val imageRef = storageRef.child("images/$eventId")
    val uploadTask = imageRef.putFile(selectedImageUri!!)

    uploadTask.continueWithTask {
      if (!it.isSuccessful) {
        it.exception?.let {
          throw it
        }
      }
      imageRef.downloadUrl
    }.addOnCompleteListener { task ->
      if (task.isSuccessful) {
        val downloadUri = task.result
        val event = event_structure(date, description, title, downloadUri.toString(), eventId)
        newEventRef.setValue(event) // Use newEventRef toset the data
        Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show()
        finish()
      } else {
        Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()

      }
      finish()
      binding.progressBar.visibility = View.GONE
      binding.buttonAdd.isEnabled = true

    }

  }

  companion object {
    private val storageRef = FirebaseStorage.getInstance().reference
  }
}