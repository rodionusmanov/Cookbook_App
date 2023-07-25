package com.example.cookbook.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cookbook.R
import com.example.cookbook.databinding.ActivityMainBinding
import com.example.cookbook.view.favorite.FavoriteFragment
import com.example.cookbook.view.myExperience.MyExperienceFragment
import com.example.cookbook.view.searchRecipe.SearchRecipeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initStartingScreen()
        initBottomNavigation()
    }

    private fun initStartingScreen() {
        navigateToFragment(SearchRecipeFragment())
    }

    private fun initBottomNavigation() {
        binding.navView.setOnItemSelectedListener { item ->
            onOptionsItemSelected(item)
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_search_recipe -> {
                navigateToFragment(SearchRecipeFragment())
            }

            R.id.navigation_favorite -> {
                navigateToFragment(FavoriteFragment())
            }

            R.id.navigation_my_experience -> {
                navigateToFragment(MyExperienceFragment())
            }
            else -> {
                navigateToFragment(SearchRecipeFragment())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, fragment)
            .addToBackStack(null)
            .commit()
    }
}