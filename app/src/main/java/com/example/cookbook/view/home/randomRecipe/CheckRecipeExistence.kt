package com.example.cookbook.view.home.randomRecipe

import kotlinx.coroutines.flow.Flow

interface CheckRecipeExistence {
    fun setRecipeIdsToWatch(ids: List<Int>)
    val recipeExistenceInDatabase: Flow<Pair<Int, Boolean>?>

}