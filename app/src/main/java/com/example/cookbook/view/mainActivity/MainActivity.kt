package com.example.cookbook.view.mainActivity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.cookbook.R
import com.example.cookbook.databinding.ActivityMainBinding
import com.example.cookbook.utils.FRAGMENT_FAVORITE
import com.example.cookbook.utils.FRAGMENT_HOME
import com.example.cookbook.utils.FRAGMENT_PROFILE
import com.example.cookbook.utils.FRAGMENT_RECIPE_INFO
import com.example.cookbook.utils.FRAGMENT_SEARCH
import com.example.cookbook.utils.navigation.OnFragmentSwitchListener
import com.example.cookbook.view.favorite.FavoriteFragment
import com.example.cookbook.view.home.HomeFragment
import com.example.cookbook.view.myProfile.MyProfileFragment
import com.example.cookbook.view.recipeInfo.RecipeInfoFragment
import com.example.cookbook.view.search.SearchFragment

class MainActivity : AppCompatActivity(), OnFragmentSwitchListener {

    private lateinit var binding: ActivityMainBinding

    private var currentFragmentTag: String? = null
    private val fragments = mapOf(
        FRAGMENT_HOME to HomeFragment(),
        FRAGMENT_SEARCH to SearchFragment(),
        FRAGMENT_FAVORITE to FavoriteFragment(),
        FRAGMENT_PROFILE to MyProfileFragment(),
        FRAGMENT_RECIPE_INFO to RecipeInfoFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigationMenu()
        if (savedInstanceState == null) {
            switchFragment(FRAGMENT_HOME, true)
        }
    }

    private fun setupBottomNavigationMenu() {
        binding.navView.setOnItemSelectedListener { item ->

            val selectedFragment = when (item.itemId) {
                R.id.navigation_home -> FRAGMENT_HOME
                R.id.navigation_search_recipe -> FRAGMENT_SEARCH
                R.id.navigation_favorite -> FRAGMENT_FAVORITE
                R.id.navigation_my_experience -> FRAGMENT_PROFILE
                else -> throw IllegalStateException("Unexpected navigation item: ${item.itemId}")
            }

            if(currentFragmentTag != selectedFragment) {
                switchFragment(selectedFragment, true)
            }
            true
        }
    }

    private fun switchFragment(tag: String, addToBackStack: Boolean = false) {
        Log.d("@@@", "Switching to fragment: $tag")
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
            Log.d("@@@", "Fragment to show: ${newFragment.tag} - isVisible: ${newFragment.isVisible}")
        }

        if(addToBackStack) {
            fragmentTransaction.addToBackStack(tag)
        }
        fragmentTransaction.commit()
        currentFragmentTag = tag
        Log.d("@@@", "Current fragment tag: ${currentFragmentTag}")

        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            val entry = supportFragmentManager.getBackStackEntryAt(i)
            Log.d("@@@", "BackStack entry $i: ${entry.name}")
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            val currentFragmentTag = currentFragmentTag
            supportFragmentManager.popBackStack()
            Log.d("@@@", "Current fragment tag on BackStack: ${currentFragmentTag}")
            val previousFragmentTag = supportFragmentManager
                .getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 2).name
            Log.d("@@@", "Previous fragment tag on BackStack: ${previousFragmentTag}")
            if (previousFragmentTag != currentFragmentTag) {
                onFragmentSwitched(previousFragmentTag)
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onFragmentSwitched(tag: String?) {
        binding.navView.setOnItemSelectedListener(null)

        val selectedItemId = when (tag) {
            FRAGMENT_HOME -> R.id.navigation_home
            FRAGMENT_SEARCH -> R.id.navigation_search_recipe
            FRAGMENT_FAVORITE -> R.id.navigation_favorite
            FRAGMENT_PROFILE -> R.id.navigation_my_experience
            FRAGMENT_RECIPE_INFO -> return
            else -> return
        }

        if (binding.navView.selectedItemId != selectedItemId) {
            binding.navView.selectedItemId = selectedItemId
        }

        currentFragmentTag = tag

        setupBottomNavigationMenu()
        Log.d("@@@", "Current fragment tag: ${currentFragmentTag}")
    }
}