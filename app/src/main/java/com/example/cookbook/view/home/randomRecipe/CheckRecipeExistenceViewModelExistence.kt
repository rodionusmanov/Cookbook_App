package com.example.cookbook.view.home.randomRecipe

import kotlinx.coroutines.flow.StateFlow

interface CheckRecipeExistenceViewModelExistence {
    fun checkRecipeExistenceInDatabase(id: Int)
    val recipeExistenceInDatabase: StateFlow<Pair<Int, Boolean>?>
}