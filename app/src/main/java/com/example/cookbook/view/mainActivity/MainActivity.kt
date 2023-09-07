package com.example.cookbook.view.mainActivity

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.cookbook.databinding.ActivityMainBinding
import com.example.cookbook.model.repository.sharedPreferences.SharedPreferencesRepository
import com.example.cookbook.utils.FRAGMENT_FIRST_LAUNCH
import com.example.cookbook.utils.FRAGMENT_HOME
import com.example.cookbook.utils.FRAGMENT_PROFILE
import com.example.cookbook.utils.navigation.NavigationManager
import com.example.cookbook.view.home.HomeFragment
import com.example.cookbook.view.myProfile.MyProfileFragment
import com.example.cookbook.view.myProfile.MyProfileViewModel
import com.example.cookbook.view.myProfile.OnProfileUpdatedListener
import com.example.cookbook.view.recipeInfo.RecipeInfoViewModel
import com.example.cookbook.view.search.SearchViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), OnProfileUpdatedListener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val sharedPrefs : SharedPreferencesRepository by inject()

    private val navigationManager: NavigationManager by lazy {
        NavigationManager(this, binding.navView).apply {
            setupBottomNavigationMenu()
        }
    }

    private val recipeInfoViewModel: RecipeInfoViewModel by viewModel()
    private val searchViewModel: SearchViewModel by viewModel()
    private val myProfileViewModel: MyProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            if (sharedPrefs.getFirstLaunch()) {
                navigationManager.switchFragment(FRAGMENT_FIRST_LAUNCH, addToBackStack = false)
            } else {
                navigationManager.switchFragment(FRAGMENT_HOME, addToBackStack = true)
            }
        }
    }

    override fun onBackPressed() {
        if (navigationManager.handleBackPressed() == null) {
            AlertDialog.Builder(this).setTitle("Exit").setMessage("Are you sure?")
                .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                .setPositiveButton("Yes") { _, _ -> super.onBackPressed() }
                .show()
        }
    }

    fun provideNavigationManager(): NavigationManager = navigationManager
    override fun onProfileUpdated(name: String, secondName: String, avatarUri: Uri?) {
        val homeFragment = supportFragmentManager.findFragmentByTag(FRAGMENT_HOME) as HomeFragment
        val profileFragment =
            supportFragmentManager.findFragmentByTag(FRAGMENT_PROFILE) as MyProfileFragment
        homeFragment.updateAvatar(avatarUri)
        profileFragment.onProfileUpdated(name, secondName, avatarUri)
    }
}