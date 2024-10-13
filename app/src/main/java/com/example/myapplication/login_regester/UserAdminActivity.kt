package com.example.myapplication.login_regester

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class UserAdminActivity : AppCompatActivity() {

  private lateinit var studentLoginButton: Button
  private lateinit var adminLoginButton: Button

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_user_admin)

    studentLoginButton = findViewById(R.id.button2)
    adminLoginButton = findViewById(R.id.button3)

    studentLoginButton.setOnClickListener {
      startActivity(Intent(this, LoginActivity::class.java))
    }

    adminLoginButton.setOnClickListener {
      startActivity(Intent(this, AdminLoginActivity::class.java))
    }
  }
}