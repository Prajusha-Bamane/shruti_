package com.example.myapplication.login_regester

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.google.firebase.firestore.FirebaseFirestore
import android.view.MotionEvent

class ResetPasswordActivity : AppCompatActivity() {

  private lateinit var etNewPasscode: EditText
  private lateinit var etConfirmNewPasscode: EditText
  private lateinit var btnResetPasscode: Button
  private lateinit var ivTogglePassword: ImageView
  private lateinit var ivToggleConfirmPassword: ImageView
  private val db = FirebaseFirestore.getInstance()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_reset_password)

    etNewPasscode = findViewById(R.id.etNewPasscode)
    etConfirmNewPasscode = findViewById(R.id.etConfirmNewPasscode)
    btnResetPasscode = findViewById(R.id.btnResetPasscode)
    ivTogglePassword = findViewById(R.id.ivTogglePassword)
    ivToggleConfirmPassword = findViewById(R.id.ivToggleConfirmPassword)

    // Set initial input type to password for both EditTexts
    etNewPasscode.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    etConfirmNewPasscode.inputType =
      InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

    // Set click listeners for eye icons
    ivTogglePassword.setOnClickListener {
      togglePasswordVisibility(
        etNewPasscode,
        ivTogglePassword
      )
    }
    ivToggleConfirmPassword.setOnClickListener {
      togglePasswordVisibility(
        etConfirmNewPasscode,
        ivToggleConfirmPassword
      )
    }

    btnResetPasscode.setOnClickListener {
      val newPasscode = etNewPasscode.text.toString().trim()
      val confirmNewPasscode = etConfirmNewPasscode.text.toString().trim()
      if (isValidPassword(newPasscode) && newPasscode == confirmNewPasscode) {
        // Passwords match, proceed with reset
        db.collection("admin").document("credentials")
          .update("passcode", newPasscode)
          .addOnSuccessListener {
            Toast.makeText(this, "Passcode reset successfully!", Toast.LENGTH_SHORT).show()
            finish()
          }
          .addOnFailureListener { e ->
            Toast.makeText(this, "Failed to reset passcode: ${e.message}", Toast.LENGTH_SHORT)
              .show()
          }
      } else {
        Toast.makeText(this, "write proper passcode", Toast.LENGTH_SHORT).show()
      }
    }
  }

  @SuppressLint("ClickableViewAccessibility")
  private fun togglePasswordVisibility(editText: EditText, imageView: ImageView) {
    imageView.setOnTouchListener { _, event ->
    when (event.action) {
      MotionEvent.ACTION_DOWN -> {
        // Show password
        editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_eye_24)) // Added semicolon here
      }
      MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
        // Hide password
        editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_eye_24))
      }
    }// Move cursor to the end of the text
    editText.setSelection(editText.text.length)
    true // Consume the touch event
  }
  }

  private fun isValidPassword(password: String): Boolean {
    if (password.length < 8) {
      return false
    }
    if (!password.contains(Regex("[A-Z]"))) { // Check for at least one capital letter
      return false
    }
    if (!password.contains(Regex("[a-z]"))) { // Check for at least one lowercase letter
      return false
    }
    if (!password.contains(Regex("[0-9]"))) { // Check for at least one digit
      return false
    }
    if (!password.contains(Regex("[^a-zA-Z0-9\\s]"))) { // Check for at least one special character
      return false
    }
    return true
  }
}