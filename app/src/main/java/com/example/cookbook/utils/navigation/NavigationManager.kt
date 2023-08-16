package com.example.cookbook.utils.navigation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cookbook.R
import com.example.cookbook.utils.FRAGMENT_FAVORITE
import com.example.cookbook.utils.FRAGMENT_HOME
import com.example.cookbook.utils.FRAGMENT_PROFILE
import com.example.cookbook.utils.FRAGMENT_RECIPE_INFO
import com.example.cookbook.utils.FRAGMENT_RECIPE_INFO_FROM_DATABASE
import com.example.cookbook.utils.FRAGMENT_SEARCH
import com.example.cookbook.utils.ID
import com.example.cookbook.view.favorite.FavoriteFragment
import com.example.cookbook.view.home.HomeFragment
import com.example.cookbook.view.myProfile.MyProfileFragment
import com.example.cookbook.view.recipeInfo.RecipeInfoFragment
import com.example.cookbook.view.recipeInfoFromDatabase.RecipeInfoFromDatabaseFragment
import com.example.cookbook.view.search.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Stack

class NavigationManager(
    private val activity: AppCompatActivity,
    private val navView: BottomNavigationView
) : OnFragmentSwitchListener {
    private var currentFragmentTag: String? = null
    private val fragments = mapOf(
        FRAGMENT_HOME to HomeFragment(),
        FRAGMENT_SEARCH to SearchFragment(),
        FRAGMENT_FAVORITE to FavoriteFragment(),
        FRAGMENT_PROFILE to MyProfileFragment(),
        FRAGMENT_RECIPE_INFO to RecipeInfoFragment(),
        FRAGMENT_RECIPE_INFO_FROM_DATABASE to RecipeInfoFromDatabaseFragment()
    )
    private val fragmentBackStack = Stack<String>()
    private var isSwitchingFragment: Boolean = false

    fun setupBottomNavigationMenu() {
        navView.setOnItemSelectedListener { item ->

            if (isSwitchingFragment) {
                isSwitchingFragment = false
                return@setOnItemSelectedListener true
            }

            val selectedFragment = when (item.itemId) {
                R.id.navigation_home -> FRAGMENT_HOME
                R.id.navigation_search_recipe -> FRAGMENT_SEARCH
                R.id.navigation_favorite -> FRAGMENT_FAVORITE
//                R.id.navigation_my_experience -> FRAGMENT_PROFILE
                else -> throw IllegalStateException("Unexpected navigation item: ${item.itemId}")
            }

            if (currentFragmentTag != selectedFragment) {
                switchFragment(selectedFragment, addToBackStack = true)
            }
            true
        }
    }

    fun switchFragment(
        tag: String,
        recipeInfoFragment: Fragment? = null,
        addToBackStack: Boolean = false
    ) {
        Log.d("@@@", "Switching to fragment: $tag")
        val fragmentTransaction = activity.supportFragmentManager.beginTransaction()

        for (fragment in activity.supportFragmentManager.fragments) {
            fragmentTransaction.hide(fragment)
        }

        var newFragment = activity.supportFragmentManager.findFragmentByTag(tag)
        if (newFragment == null || tag == FRAGMENT_RECIPE_INFO) {
            newFragment = recipeInfoFragment ?: fragments[tag]
            if (newFragment != null) {
                fragmentTransaction.add(R.id.main_container, newFragment, tag)
            }
        } else if (newFragment == null || tag == FRAGMENT_RECIPE_INFO_FROM_DATABASE) {
            newFragment = recipeInfoFragment ?: fragments[tag]
            if (newFragment != null) {
                fragmentTransaction.add(R.id.main_container, newFragment, tag)
            }
        } else {
            fragmentTransaction.show(newFragment)
            Log.d(
                "@@@",
                "Fragment to show: ${newFragment.tag} - isVisible: ${newFragment.isVisible}"
            )
        }

        fragmentTransaction.commit()
        currentFragmentTag = tag
        Log.d("@@@", "Current fragment tag: $currentFragmentTag")

        onFragmentSwitched(tag)

        if (addToBackStack) {
            pushFragmentToStack(tag)
        }

        for (i in fragmentBackStack.indices) {
            val entry = fragmentBackStack[i]
            Log.d("@@@", "BackStack entry $i: $entry")
        }
    }

    override fun onFragmentSwitched(tag: String?) {
        val selectedItemId = when (tag) {
            FRAGMENT_HOME -> R.id.navigation_home
            FRAGMENT_SEARCH -> R.id.navigation_search_recipe
            FRAGMENT_FAVORITE -> R.id.navigation_favorite
//            FRAGMENT_PROFILE -> R.id.navigation_my_experience
            FRAGMENT_RECIPE_INFO -> return
            else -> return
        }

        if (navView.selectedItemId != selectedItemId) {
            isSwitchingFragment = true
            navView.selectedItemId = selectedItemId
        }

        currentFragmentTag = tag

        Log.d("@@@", "Current fragment tag: $currentFragmentTag")
    }

    override fun pushFragmentToStack(tag: String) {
        fragmentBackStack.push(tag)
    }

    fun handleBackPressed(): Boolean? {
        val previousFragmentTag = popFragmentFromStack()
        return if (previousFragmentTag != null) {
            switchFragment(previousFragmentTag)
            true
        } else {
            null
        }
    }

    override fun popFragmentFromStack(): String? {
        return if (fragmentBackStack.size > 1) {
            fragmentBackStack.pop()
            fragmentBackStack.peek()
        } else null
    }

    fun openRecipeInfoFragment(recipeId: Int) {
        val recipeInfoFragment = RecipeInfoFragment.newInstance()
        val bundle = Bundle().apply {
            putInt(ID, recipeId)
        }
        recipeInfoFragment.arguments = bundle
        switchFragment(
            FRAGMENT_RECIPE_INFO,
            recipeInfoFragment = recipeInfoFragment,
            addToBackStack = true
        )
    }

    fun openRecipeInfoFromDatabaseFragment(recipeId: Int) {
        val recipeInfoFromDatabaseFragment = RecipeInfoFromDatabaseFragment.newInstance()
        val bundle = Bundle().apply {
            putInt(ID, recipeId)
        }
        recipeInfoFromDatabaseFragment.arguments = bundle
        switchFragment(
            FRAGMENT_RECIPE_INFO_FROM_DATABASE,
            recipeInfoFragment = recipeInfoFromDatabaseFragment,
            addToBackStack = true
        )
    }
}