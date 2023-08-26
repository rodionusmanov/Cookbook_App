package com.example.cookbook.model.repository.local

import com.example.cookbook.model.domain.RecipeInformation
import com.example.cookbook.model.room.fullRecipe.IRecipesInfoDAO
import com.example.cookbook.model.room.fullRecipe.RecipeInfoEntity
import com.example.cookbook.utils.convertRecipeInfoEntityToList
import com.example.cookbook.utils.convertRecipeInfoEntityToRecipeInformation
import com.example.cookbook.utils.convertRecipeInfoToEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

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

}