package com.example.cookbook.view.recipeInfo.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cookbook.model.datasource.DTO.recipeInformation.AnalyzedInstruction
import com.example.cookbook.model.datasource.DTO.recipeInformation.ExtendedIngredient
import com.example.cookbook.model.domain.RecipeInformation
import com.example.cookbook.utils.INGREDIENTS
import com.example.cookbook.utils.INSTRUCTIONS
import com.example.cookbook.view.recipeInfo.ingredient.IngredientFragment
import com.example.cookbook.view.recipeInfo.preparation.PreparationFragment

class RecipeInformationPageAdapter(
    private val data: RecipeInformation,
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> IngredientFragment().let {
                it.arguments = Bundle().apply {
                    putParcelableArrayList(
                        INGREDIENTS,
                        ArrayList<ExtendedIngredient>(data.extendedIngredients)
                    )
                }
                return it
            }

            else -> PreparationFragment().let {
                it.arguments = Bundle().apply {
                    putParcelableArrayList(
                        INSTRUCTIONS,
                        ArrayList<AnalyzedInstruction>(data.analyzedInstructions)
                    )
                }
                return it
            }
        }
    }


}