package com.example.cookbook.view.searchRecipe

import com.example.cookbook.model.domain.BaseRecipeData

interface ISaveRecipe {
    fun saveRecipe(recipe: BaseRecipeData)
}