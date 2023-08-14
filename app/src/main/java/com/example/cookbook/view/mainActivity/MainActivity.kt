package com.example.cookbook.view.mainActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.cookbook.R
import com.example.cookbook.databinding.ActivityMainBinding
import com.example.cookbook.view.favorite.FavoriteFragment
import com.example.cookbook.view.home.HomeFragment
import com.example.cookbook.view.search.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var navView: BottomNavigationView

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
                when(it){
                    is HomeFragment -> {
                        navView.menu.get(0).isChecked = true
                    }
                    is SearchFragment -> {
                        navView.menu.get(1).isChecked = true
                    }
                    is FavoriteFragment -> {
                        navView.menu.get(2).isChecked = true
                    }
                }
            }
        }

        supportFragmentManager.addFragmentOnAttachListener { fragmentManager, fragment ->
            fragmentManager.fragments.forEach {
                when(it){
                    is HomeFragment -> {
                        navView.menu.get(0).isChecked = true
                    }
                    is SearchFragment -> {
                        navView.menu.get(1).isChecked = true
                    }
                    is FavoriteFragment -> {
                        navView.menu.get(2).isChecked = true
                    }
                }
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
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}