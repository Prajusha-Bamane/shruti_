package com.example.myapplication


import android.content.Intent
import androidx.activity.OnBackPressedCallback
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.ActivityMainBinding
import nav_fregment.aboutus
import nav_fregment.event
import nav_fregment.home
import nav_fregment.profile
import nav_fregment.student_volunter
import nav_fregment.volunter


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fregmemt(home(), true)


        binding.bottnavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.about -> fregmemt(aboutus(), false)
                R.id.volunter -> fregmemt(student_volunter(), false)
                R.id.home -> fregmemt(volunter(), true)
                R.id.events -> fregmemt(event(), false)
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
                R.id.nav_contact ->{
                        val intent = Intent(this, contact::class.java)
                        startActivity(intent)
                    true
                }
                R.id.nav_support ->{
                    val intent = Intent(this, Support::class.java)
                    startActivity(intent)
                    true
                }

                else -> {false}
            }
        }


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


}