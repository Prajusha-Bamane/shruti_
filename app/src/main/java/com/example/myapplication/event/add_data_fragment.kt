package com.example.myapplication.event

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAddDataFragmentBinding
import com.example.myapplication.moduel.event_structure
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class add_data_fragment : Fragment() {

  private lateinit var binding: FragmentAddDataFragmentBinding
  private var eventref = FirebaseDatabase.getInstance().getReference().child("Event")
  private var selectedImageUri: Uri? = null

  private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
    binding.imageView.setImageURI(uri)
    selectedImageUri = uri
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentAddDataFragmentBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

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
      Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
      return
    }

    binding.buttonAdd.isEnabled = false
    binding.progressBar.visibility = View.VISIBLE

    if (selectedImageUri == null) {
      Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
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
        newEventRef.setValue(event)

        // Send notification to all users
        sendNotificationToAllUsers(event)

        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.remove(this)
        fragmentTransaction.add(R.id.relative, event_mam())
        fragmentTransaction.commit()

        Toast.makeText(context, "Data inserted successfully", Toast.LENGTH_SHORT).show()
      } else {
        Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
      }
      binding.progressBar.visibility = View.GONE
      binding.buttonAdd.isEnabled = true
    }
  }

  private fun sendNotificationToAllUsers(event: event_structure) {
    val intent = Intent(requireContext(), EventNotificationService::class.java)
    intent.putExtra("eventTitle", event.title)
    intent.putExtra("eventDescription", event.description)
    requireContext().startService(intent)
    Log.d("Notification", "Notification sent: ${event.title} - ${event.description}")
  }

  companion object {
    private val storageRef = FirebaseStorage.getInstance().reference
  }
}