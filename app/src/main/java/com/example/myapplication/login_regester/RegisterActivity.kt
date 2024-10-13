package com.example.myapplication.login_regester

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegisterActivity : AppCompatActivity() {

  private lateinit var databaseReference: DatabaseReference

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_register)

    databaseReference = FirebaseDatabase.getInstance()
      .getReferenceFromUrl("https://my-application-8434b-default-rtdb.firebaseio.com/Users")

    val Name = findViewById<EditText>(R.id.Name)
    val UserId = findViewById<EditText>(R.id.UserID)
    val Password = findViewById<EditText>(R.id.Password)
    val ConPassword = findViewById<EditText>(R.id.ConPassword)
    val registerBtn = findViewById<Button>(R.id.Register)

    registerBtn.setOnClickListener { v: View? ->
      val nameTxt = Name.text.toString()
      val UserIdTxt = UserId.text.toString()
      val PasswordTxt = Password.text.toString()
      val ConPasswordTxt = ConPassword.text.toString()

      if (!validateInputs(nameTxt, UserIdTxt, PasswordTxt, ConPasswordTxt)) {
        return@setOnClickListener
      }

      databaseReference.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
          if (dataSnapshot.hasChild(UserIdTxt)) {
            Toast.makeText(this@RegisterActivity, "User is Already Registered", Toast.LENGTH_SHORT).show()
          } else {
            val userValues = HashMap<String, Any>()
            userValues["name"] = nameTxt
            userValues["password"] = PasswordTxt

            databaseReference.child("users").child(UserIdTxt).updateChildren(userValues)
              .addOnSuccessListener {
                Toast.makeText(this@RegisterActivity, "User Registered Successfully", Toast.LENGTH_SHORT).show()
                finish()
              }
              .addOnFailureListener { e ->
                Toast.makeText(this@RegisterActivity, "Failed to register: ${e.message}", Toast.LENGTH_SHORT).show()
              }
          }
        }

        override fun onCancelled(databaseError: DatabaseError) {
          Toast.makeText(this@RegisterActivity, "Failed to register: ${databaseError.message}", Toast.LENGTH_SHORT).show()
        }
      })
    }
  }

  private fun validateInputs(name: String, userId: String, password: String, conPassword: String): Boolean {
    if (name.isEmpty() || userId.isEmpty() || password.isEmpty() || conPassword.isEmpty()) {
      Toast.makeText(this, "Please Fill All Details", Toast.LENGTH_SHORT).show()
      return false
    }

    if (password != conPassword) {
      Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
      return false
    }

    return true
  }
}