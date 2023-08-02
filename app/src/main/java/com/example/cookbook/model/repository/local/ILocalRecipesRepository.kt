package com.example.cookbook.model.repository.local

import com.example.cookbook.model.domain.SearchRecipeData

interface ILocalRecipesRepository {

    suspend fun insertNewRecipe(recipeData: SearchRecipeData)

    suspend fun getAllRecipesData(): List<SearchRecipeData>

    suspend fun removeRecipeFromData(id: Int)
}