package com.example.cookbook.view.mainActivity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.cookbook.R
import com.example.cookbook.databinding.ActivityMainBinding
import com.example.cookbook.view.favorite.FavoriteFragment
import com.example.cookbook.view.home.HomeFragment
import com.example.cookbook.view.myProfile.MyProfileFragment
import com.example.cookbook.view.search.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private var currentFragmentTag: String? = null
    private val fragments = mapOf(
        "home" to HomeFragment(),
        "search_recipe" to SearchFragment(),
        "favorite" to FavoriteFragment(),
        "my_experience" to MyProfileFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigationMenu()
        if (savedInstanceState == null) {
            switchFragment("home")
        }
    }

    private fun setupBottomNavigationMenu() {
        binding.navView.setOnItemSelectedListener { item ->
            val selectedFragment = when(item.itemId) {
                R.id.navigation_home -> "home"
                R.id.navigation_search_recipe -> "search_recipe"
                R.id.navigation_favorite -> "favorite"
                R.id.navigation_my_experience -> "my_experience"
                else -> throw IllegalStateException("Unexpected navigation item: ${item.itemId}")
            }
            switchFragment(selectedFragment)
            true
        }
    }

    private fun switchFragment(tag: String) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        currentFragmentTag?.let { currentTag ->
            val currentFragment = supportFragmentManager.findFragmentByTag(currentTag)
            if (currentFragment != null) {
                fragmentTransaction.hide(currentFragment) }
        }

        val newFragment = supportFragmentManager.findFragmentByTag(tag)
        if (newFragment != null) {
            fragmentTransaction.show(newFragment)
        } else {
            fragments[tag]?.let {fragment ->
                fragmentTransaction.add(R.id.main_container, fragment, tag)
            }
        }

        fragmentTransaction.commit()
        currentFragmentTag = tag
    }

    fun setSelectedNavigationItem(itemId: Int) {
        binding.navView.selectedItemId = itemId
    }

    fun printBackStack() {
        val fragments = supportFragmentManager.fragments
        for (fragment in fragments) {
            Log.d("Navigation", "Fragment: ${fragment::class.java.simpleName}")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}