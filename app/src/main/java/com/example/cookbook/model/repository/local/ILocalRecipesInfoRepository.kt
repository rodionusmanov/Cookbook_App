package com.example.cookbook.model.repository.local

import com.example.cookbook.model.domain.RecipeInformation

interface ILocalRecipesInfoRepository {

    suspend fun upsertNewRecipe(recipeData: RecipeInformation)

    suspend fun getAllRecipesData(): List<RecipeInformation>

    suspend fun getRecipeDataById(id: Int): RecipeInformation

    suspend fun removeRecipeFromData(id: Int)
}