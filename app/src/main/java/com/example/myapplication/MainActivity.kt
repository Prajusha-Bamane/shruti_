package com.example.myapplication


import android.content.Intent
import androidx.fragment.app.FragmentManager
import androidx.activity.OnBackPressedCallback
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.ActivityMainBinding
import nav_fregment.aboutus
import nav_fregment.event
import nav_fregment.profile
import nav_fregment.volunter


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.bottnavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.about -> fregmemt(aboutus(), true)
                R.id.volunter -> fregmemt(volunter(), false)

                R.id.events -> fregmemt(event(), false)
                R.id.profile -> fregmemt(profile(), false)
            }
            true
        }


        binding.fabutton.setOnClickListener {
            navigateToInitialFragment()
        }
            // menu button
        binding.menu.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(binding.navigation)) {
                binding.drawerLayout.closeDrawer(binding.navigation)
            } else {
                binding.drawerLayout.openDrawer(binding.navigation)
            }
        }

        // Handle back press using OnBackPressedCallback
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(binding.navigation)) {
                    binding.drawerLayout.closeDrawer(binding.navigation)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed() //Let the system handle the back press
                }
            }
        })

        binding.navigation.setNavigationItemSelectedListener {
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

    fun navigateToInitialFragment() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        binding.bottnavigation.selectedItemId = R.id.about
    }

    fun fregmemt(fragment: Fragment, flag: Boolean) {
        val trasnaction = supportFragmentManager.beginTransaction()
        if (flag) {
            trasnaction.add(binding.relative.id, fragment)
        } else {
            trasnaction.replace(binding.relative.id, fragment)
        }
        trasnaction.commit()
    }
}