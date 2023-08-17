package com.example.cookbook.view.mainActivity

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.cookbook.R
import com.example.cookbook.databinding.ActivityMainBinding
import com.example.cookbook.view.favorite.FavoriteFragment
import com.example.cookbook.view.home.HomeFragment
import com.example.cookbook.view.recipeInfo.RecipeInfoViewModel
import com.example.cookbook.view.search.SearchFragment
import com.example.cookbook.view.search.SearchViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var navView: BottomNavigationView

    private val recipeInfoViewModel: RecipeInfoViewModel by viewModel()
    private val searchViewModel: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navView = binding.navView

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.main_container, HomeFragment.newInstance())
                .commit()
        }


        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    openFragment(HomeFragment())
                }

                R.id.navigation_search_recipe -> {
                    openFragment(SearchFragment())
                }

                R.id.navigation_favorite -> {
                    openFragment(FavoriteFragment())
                }
            }
            true
        }
        supportFragmentManager.addOnBackStackChangedListener {
            supportFragmentManager.fragments.forEach {
                fragmentChanger(it)
            }
        }

        supportFragmentManager.addFragmentOnAttachListener { fragmentManager, _ ->
            fragmentManager.fragments.forEach {
                fragmentChanger(it)
            }
        }
    }

    private fun fragmentChanger(fragment: Fragment) {
        when (fragment) {
            is HomeFragment -> {
                navView.menu[0].isChecked = true
            }

            is SearchFragment -> {
                navView.menu[1].isChecked = true
            }

            is FavoriteFragment -> {
                navView.menu[2].isChecked = true
            }
        }
    }


    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment, fragment.javaClass.simpleName)
            .addToBackStack(fragment.tag)
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            AlertDialog.Builder(this).setTitle("Exit").setMessage("Are you sure?")
                .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                .setPositiveButton("Yes") { _, _ -> super.onBackPressed() }
                .show()

        } else {
            supportFragmentManager.popBackStack()
        }
    }
}