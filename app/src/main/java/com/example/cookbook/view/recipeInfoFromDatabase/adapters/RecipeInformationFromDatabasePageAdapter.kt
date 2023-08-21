package com.example.cookbook.view.recipeInfo.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cookbook.view.recipeInfoFromDatabase.ingredient.IngredientFromDatabaseFragment
import com.example.cookbook.view.recipeInfo.preparation.PreparationFromDatabaseFragment

class RecipeInformationFromDatabasePageAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> IngredientFromDatabaseFragment()

            else -> PreparationFromDatabaseFragment()
        }
    }
}