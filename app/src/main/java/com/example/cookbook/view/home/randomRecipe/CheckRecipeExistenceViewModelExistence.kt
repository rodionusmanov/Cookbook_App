package com.example.cookbook.view.home.randomRecipe

import kotlinx.coroutines.flow.StateFlow

interface CheckRecipeExistenceViewModelExistence {
    fun observeRecipeExistenceInDatabase(id: Int)
    val recipeExistenceInDatabase: StateFlow<Pair<Int, Boolean>?>

}