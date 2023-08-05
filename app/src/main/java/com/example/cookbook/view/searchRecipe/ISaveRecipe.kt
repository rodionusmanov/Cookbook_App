package com.example.cookbook.view.searchRecipe

import com.example.cookbook.model.domain.SearchRecipeData
import java.text.FieldPosition

interface ISaveRecipe {
    fun saveRecipe(recipe: SearchRecipeData)
}