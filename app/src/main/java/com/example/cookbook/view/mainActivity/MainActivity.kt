package com.example.cookbook.view.mainActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cookbook.R
import com.example.cookbook.databinding.ActivityMainBinding
import com.example.cookbook.utils.navigation.OnFragmentSwitchListener
import com.example.cookbook.view.favorite.FavoriteFragment
import com.example.cookbook.view.home.HomeFragment
import com.example.cookbook.view.myProfile.MyProfileFragment
import com.example.cookbook.view.search.SearchFragment

class MainActivity : AppCompatActivity(), OnFragmentSwitchListener {

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
            switchFragment("home", true)
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

            if(currentFragmentTag != selectedFragment) {
                switchFragment(selectedFragment, true)
            }
            true
        }
    }

    private fun switchFragment(tag: String, addToBackStack: Boolean = false) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        for (fragment in supportFragmentManager.fragments) {
            fragmentTransaction.hide(fragment)
        }

        var newFragment = supportFragmentManager.findFragmentByTag(tag)
        if (newFragment == null) {
            newFragment = fragments[tag]
            if(newFragment != null){
                fragmentTransaction.add(R.id.main_container, newFragment, tag)
            }
        } else {
            fragmentTransaction.show(newFragment)
        }

        if(addToBackStack) {
            fragmentTransaction.addToBackStack(tag)
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

    override fun onFragmentSwitched(tag: String?) {
        val selectedItemId = when (tag) {
            "home" -> R.id.navigation_home
            "search_recipe" -> R.id.navigation_search_recipe
            "favorite" -> R.id.navigation_favorite
            "my_experience" -> R.id.navigation_my_experience
            "recipeInfo" -> return
            else -> return
        }

        if (binding.navView.selectedItemId != selectedItemId) {
            binding.navView.selectedItemId = selectedItemId
        }

        currentFragmentTag = tag
    }
}