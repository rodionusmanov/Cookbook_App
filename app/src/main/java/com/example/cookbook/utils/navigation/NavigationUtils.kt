package com.example.cookbook.utils.navigation

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.cookbook.R
import com.example.cookbook.utils.FRAGMENT_RECIPE_INFO
import com.example.cookbook.utils.FRAGMENT_SEARCH
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

            for (fragment in fragmentManager.fragments) {
                hide(fragment)
            }

            add(R.id.main_container, recipeInfoFragment, FRAGMENT_RECIPE_INFO)
            addToBackStack(FRAGMENT_RECIPE_INFO)
            commit()
        }

        listener.onFragmentSwitched(FRAGMENT_RECIPE_INFO)
        listener.pushFragmentToStack(FRAGMENT_RECIPE_INFO)
    }

    fun navigateToSearchFragmentWithQuery(
        fragmentManager: FragmentManager,
        listener: OnFragmentSwitchListener,
        containerId: Int,
        destinedFragment: Fragment,
        queryKey: String,
        queryValue: String,
        tag: String = FRAGMENT_SEARCH
    ) {
        val bundle = bundleOf(queryKey to queryValue)
        destinedFragment.arguments = bundle

        fragmentManager.beginTransaction().apply {
            for (fragment in fragmentManager.fragments) {
                if (fragment.tag != tag) {
                    hide(fragment)
                }
            }

            if (fragmentManager.findFragmentByTag(tag) != null) {
                show(fragmentManager.findFragmentByTag(tag)!!)
            } else {
                add(containerId, destinedFragment, tag)
            }
            commit()
            listener.onFragmentSwitched(tag)
            listener.pushFragmentToStack(tag)
        }
    }
}