package com.example.cookbook.model.repository.local

import com.example.cookbook.model.domain.RecipeInformation
import com.example.cookbook.model.room.RecipeInfoEntity
import kotlinx.coroutines.flow.Flow

interface ILocalRecipesInfoRepository {

    suspend fun upsertNewRecipe(recipeData: RecipeInformation)
    fun getAllRecipesData(): Flow<List<RecipeInfoEntity>>

    suspend fun getRecipeDataById(id: Int): RecipeInformation

    suspend fun removeRecipeFromData(id: Int)

    suspend fun checkExistence(id: Int): Boolean

    fun observeRecipeExistence(id: Int): Flow<Boolean>
}