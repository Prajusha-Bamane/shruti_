package com.example.myapplication.login_regester

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

  private lateinit var userNameEditText: EditText
  private lateinit var passwordEditText: TextInputEditText
  private lateinit var loginButton: Button
  private lateinit var databaseReference: DatabaseReference

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    userNameEditText = findViewById(R.id.user_name)
    passwordEditText = findViewById(R.id.password_input)
    loginButton = findViewById(R.id.login_btn)

    databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://my-application-8434b-default-rtdb.firebaseio.com/Users")

    loginButton.setOnClickListener { loginUser() }
  }

  private fun loginUser() {
    val userId = userNameEditText.text.toString().trim()
    val passwordStr = passwordEditText.text.toString().trim()

    if (!validateInputs(userId, passwordStr)) {
      return
    }

    databaseReference.child("users").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        if (dataSnapshot.exists()) {
          val retrievedPassword = dataSnapshot.child("password").getValue(String::class.java)

          if (retrievedPassword != null && retrievedPassword == passwordStr) {
            Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
          } else {
            Toast.makeText(this@LoginActivity, "Incorrect password", Toast.LENGTH_SHORT).show()
          }
        } else {
          Toast.makeText(this@LoginActivity, "User not found", Toast.LENGTH_SHORT).show()
        }
      }

      override fun onCancelled(databaseError: DatabaseError) {
        Toast.makeText(this@LoginActivity, "Database error: " + databaseError.message, Toast.LENGTH_SHORT).show()
      }
    })
  }

  private fun validateInputs(userId: String, password: String): Boolean {
    if (userId.isEmpty()) {
      Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show()
      return false
    }

    if (password.isEmpty()) {
      Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
      return false
    }

    return true
  }
}