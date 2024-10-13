package com.example.myapplication.man_side

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.Support
import com.example.myapplication.contact
import com.example.myapplication.databinding.ActivityMamSideBinding
import com.example.myapplication.event.event_mam
import com.example.myapplication.login_regester.AdminLoginActivity
import com.example.myapplication.login_regester.RegisterActivity
import com.example.myapplication.login_regester.ResetPasswordActivity
import nav_fregment.aboutus
import nav_fregment.home
import nav_fregment.profile
import com.example.myapplication.volunter.volunter
import java.io.File

class mam_side : AppCompatActivity() {
  lateinit var binding: ActivityMamSideBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    binding = ActivityMamSideBinding.inflate(layoutInflater)
    setContentView(binding.root)

    if (intent.extras != null && intent.extras!!.containsKey("fragment_to_show")) {
      val fragmentToShow = intent.getStringExtra("fragment_to_show")
      if (fragmentToShow == "event_mam") {
        fregmemt(event_mam(), false) // Navigate to event_mam fragment
      }
    } else {
      // Default fragment to show
      fregmemt(home(), true)
    }



    binding.bottnavigation.setOnItemSelectedListener {
      when (it.itemId) {
        R.id.about -> fregmemt(aboutus(), false)
        R.id.volunter -> fregmemt(volunter(), false)
        R.id.home -> fregmemt(home(), true)
        R.id.events -> fregmemt(event_mam(), false)
        R.id.profile -> fregmemt(profile(), false)
      }
      true
    }

    // menu button
    binding.toolbar.setOnClickListener {
      if (binding.drawerLayout.isDrawerOpen(binding.sidemenu)) {
        binding.drawerLayout.closeDrawer(binding.sidemenu)
      } else {
        binding.drawerLayout.openDrawer(binding.sidemenu)
      }
    }

    onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
      override fun handleOnBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(binding.sidemenu)) {
          binding.drawerLayout.closeDrawer(binding.sidemenu)
        } else {
          isEnabled = false
          onBackPressedDispatcher.onBackPressed()
        }
      }
    })

    binding.sidemenu.setNavigationItemSelectedListener {
      when (it.itemId) {
        R.id.nav_contact -> {
          val intent = Intent(this, contact::class.java)
          startActivity(intent)
          true
        }

        R.id.nav_support -> {
          val intent = Intent(this, Support::class.java)
          startActivity(intent)
          true
        }

        R.id.nav_logout -> {
          logoutAdmin()
          true
        }

        R.id.regester -> {
          val intent = Intent(this, RegisterActivity::class.java)
          startActivity(intent)
          true
        }

        R.id.ForgetPassword -> {
          val intent = Intent(this, ResetPasswordActivity::class.java)
          startActivity(intent)
          true
        }

        R.id.nav_presenty -> {
          openExcelFile()
          true
        }

        else -> false
      }
    }

    // Control visibility of Forget Password item
    val menu = binding.sidemenu.menu
    val forgetPasswordItem = menu.findItem(R.id.ForgetPassword)
    val preferences = getSharedPreferences("AdminPrefs", MODE_PRIVATE)
    val isSuperAdmin = preferences.getBoolean("isSuperAdmin", false)
    forgetPasswordItem.isVisible = isSuperAdmin
  }


  private fun fregmemt(fragment: Fragment, flag: Boolean) {
    val transaction = supportFragmentManager.beginTransaction()
    if (flag) {
      transaction.replace(binding.relative.id, fragment) // Use replace here
    } else {
      transaction.replace(binding.relative.id, fragment)
    }
    transaction.commit()
  }

  private fun logoutAdmin() {
    val preferences = getSharedPreferences("AdminPrefs", MODE_PRIVATE)
    val editor = preferences.edit()
    editor.putBoolean("isLoggedIn", false)
    editor.putBoolean("isSuperAdmin", false) // Reset super admin status as well
    editor.apply()

    // Navigate back to the login screen
    val intent = Intent(this, AdminLoginActivity::class.java)
    startActivity(intent)
    finish()
  }

  private fun openExcelFile() {
    val excelFile = File(getExternalFilesDir(null), "preasenty.xlsx")

    val uri = FileProvider.getUriForFile(
      this,
      "${packageName}.fileprovider",
      excelFile
    )

    val intent = Intent(Intent.ACTION_VIEW).apply {
      setDataAndType(uri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
      addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    try {
      startActivity(intent)
    } catch (e: ActivityNotFoundException) {
      Toast.makeText(this, "No app found to open Excel files", Toast.LENGTH_SHORT).show()
    }
  }
}
