package com.example.cookbook.model.repository.local

import com.example.cookbook.model.domain.RecipeInformation
import com.example.cookbook.model.room.fullRecipe.IRecipesInfoDAO
import com.example.cookbook.model.room.fullRecipe.RecipeInfoEntity
import com.example.cookbook.utils.convertRecipeInfoEntityToRecipeInformation
import com.example.cookbook.utils.convertRecipeInfoToEntity
import kotlinx.coroutines.flow.Flow

class LocalRepositoryInfoImpl(private val recipeInfoDAO: IRecipesInfoDAO) :
    ILocalRecipesInfoRepository {
    override suspend fun upsertNewRecipe(recipeData: RecipeInformation) {
        recipeInfoDAO.upsertRecipeToFavorite(convertRecipeInfoToEntity(recipeData))
    }

    override fun getAllRecipesData(): Flow<List<RecipeInfoEntity>> {
        return recipeInfoDAO.getAllFavoriteRecipes()
    }

    override suspend fun getRecipeDataById(id: Int): RecipeInformation {
        return convertRecipeInfoEntityToRecipeInformation(recipeInfoDAO.getFavoriteRecipeById(id))
    }

    override suspend fun removeRecipeFromData(id: Int) {
        recipeInfoDAO.deleteRecipeFromFavorite(id)
    }

    override suspend fun checkExistence(id: Int): Boolean {
        return recipeInfoDAO.exists(id)
    }

    override fun observeRecipeExistence(id: Int): Flow<Boolean> {
        return recipeInfoDAO.observeExistence(id)
    }
}