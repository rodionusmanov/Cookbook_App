package com.example.cookbook.view.mainActivity

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.cookbook.databinding.ActivityMainBinding
import com.example.cookbook.utils.FRAGMENT_HOME
import com.example.cookbook.utils.navigation.NavigationManager
import com.example.cookbook.view.recipeInfo.RecipeInfoViewModel
import com.example.cookbook.view.search.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navigationManager: NavigationManager

    private val recipeInfoViewModel: RecipeInfoViewModel by viewModel()
    private val searchViewModel: SearchViewModel by viewModel()

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

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
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

    fun provideNavigationManager(): NavigationManager = navigationManager

}