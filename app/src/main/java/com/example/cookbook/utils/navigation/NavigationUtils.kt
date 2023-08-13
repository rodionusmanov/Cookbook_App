package com.example.cookbook.utils.navigation

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.cookbook.R
import com.example.cookbook.utils.ID
import com.example.cookbook.view.recipeInfo.RecipeInfoFragment

object NavigationUtils {
    fun openRecipeInfoFragment(
        fragmentManager: FragmentManager,
        listener: OnFragmentSwitchListener,
        recipeId: Int
    ) {
        val recipeInfoFragment = RecipeInfoFragment.newInstance()

        val bundle = Bundle().apply {
            putInt(ID, recipeId)
        }
        recipeInfoFragment.arguments = bundle
        fragmentManager.beginTransaction().apply {
            replace(R.id.main_container, recipeInfoFragment)
            addToBackStack(null)
            commit()
        }

        listener.onFragmentSwitched("recipeInfo")
    }

    fun navigateToSearchFragmentWithQuery(
        fragmentManager: FragmentManager,
        listener: OnFragmentSwitchListener,
        containerId: Int,
        destinedFragment: Fragment,
        queryKey: String,
        queryValue: String,
        tag: String? = null
        ) {
        val bundle = bundleOf(queryKey to queryValue)
        destinedFragment.arguments = bundle

        for (fragment in fragmentManager.fragments){
            fragmentManager.beginTransaction().hide(fragment).commit()
        }

        fragmentManager.beginTransaction().apply {
            if (tag != null && fragmentManager.findFragmentByTag(tag) != null){
                replace(containerId, destinedFragment, tag)
            } else {
                add(containerId, destinedFragment, tag)
            }
            addToBackStack("search_recipe")
            commit()
        }

        listener.onFragmentSwitched("search_recipe")
    }
}