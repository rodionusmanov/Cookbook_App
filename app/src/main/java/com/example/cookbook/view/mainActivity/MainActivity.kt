package com.example.cookbook.view.mainActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cookbook.databinding.ActivityMainBinding
import com.example.cookbook.utils.FRAGMENT_HOME
import com.example.cookbook.utils.navigation.NavigationManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navigationManager: NavigationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigationManager = NavigationManager(this, binding.navView)
        navigationManager.setupBottomNavigationMenu()

        if (savedInstanceState == null) {
            navigationManager.switchFragment(FRAGMENT_HOME, addToBackStack = true)
        }
    }

    override fun onBackPressed() {
        if(navigationManager.handleBackPressed() == null) {
            super.onBackPressed()
        }
    }

    fun provideNavigationManager(): NavigationManager = navigationManager

}