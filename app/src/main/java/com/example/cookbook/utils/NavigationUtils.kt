package com.example.cookbook.utils

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.example.cookbook.R
import com.example.cookbook.view.recipeInfo.RecipeInfoFragment

object NavigationUtils {
    fun openRecipeInfoFragment(
        fragmentManager: FragmentManager,
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
    }
}