package com.example.cookbook.view.mainActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cookbook.R
import com.example.cookbook.databinding.ActivityMainBinding
import com.example.cookbook.view.favorite.FavoriteFragment
import com.example.cookbook.view.home.HomeFragment
import com.example.cookbook.view.myProfile.MyProfileFragment
import com.example.cookbook.view.search.SearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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
            val selectedFragment = when (item.itemId) {
                R.id.navigation_home -> "home"
                R.id.navigation_search_recipe -> "search_recipe"
                R.id.navigation_favorite -> "favorite"
                R.id.navigation_my_experience -> "my_experience"
                else -> throw IllegalStateException("Unexpected navigation item: ${item.itemId}")
            }
            if (currentFragmentTag != selectedFragment) {
                switchFragment(selectedFragment, true)
            } else {
                switchFragment(selectedFragment)
            }
            true
        }
        supportFragmentManager.addOnBackStackChangedListener {
            when (supportFragmentManager.findFragmentById(R.id.main_container)) {
                is HomeFragment -> binding.navView.selectedItemId = R.id.navigation_home
                is SearchFragment -> binding.navView.selectedItemId = R.id.navigation_search_recipe
                is FavoriteFragment -> binding.navView.selectedItemId = R.id.navigation_favorite
                is MyProfileFragment -> binding.navView.selectedItemId =
                    R.id.navigation_my_experience
            }
        }
    }

    private fun switchFragment(tag: String, addToBackStack: Boolean = false) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        for (fragment in supportFragmentManager.fragments) {
            fragmentTransaction.hide(fragment)
        }

        val newFragment = supportFragmentManager.findFragmentByTag(tag)
        if (newFragment != null) {
            fragmentTransaction.show(newFragment)
        } else {
            fragments[tag]?.let { fragment ->
                fragmentTransaction.add(R.id.main_container, fragment, tag)
            }
        }
        if(addToBackStack) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()
        currentFragmentTag = tag
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}