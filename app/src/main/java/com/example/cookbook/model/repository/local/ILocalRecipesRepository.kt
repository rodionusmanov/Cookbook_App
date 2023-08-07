package com.example.cookbook.model.repository.local

import com.example.cookbook.model.domain.BaseRecipeData

interface ILocalRecipesRepository {

    suspend fun insertNewRecipe(recipeData: BaseRecipeData)

    suspend fun getAllRecipesData(): List<BaseRecipeData>

    suspend fun removeRecipeFromData(id: Int)
}