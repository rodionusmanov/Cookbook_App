package com.example.cookbook.model.repository.local

import com.example.cookbook.model.domain.RecipeInformation
import com.example.cookbook.model.room.fullRecipe.IRecipesInfoDAO
import com.example.cookbook.utils.convertRecipeInfoEntityToList
import com.example.cookbook.utils.convertRecipeInfoEntityToRecipeInformation
import com.example.cookbook.utils.convertRecipeInfoToEntity

class LocalRepositoryInfoImpl(private val recipeInfoDAO: IRecipesInfoDAO) :
    ILocalRecipesInfoRepository {
    override suspend fun upsertNewRecipe(recipeData: RecipeInformation) {
        recipeInfoDAO.upsertRecipeToFavorite(convertRecipeInfoToEntity(recipeData))
    }

    override suspend fun getAllRecipesData(): List<RecipeInformation> {
        return convertRecipeInfoEntityToList(recipeInfoDAO.getAllFavoriteRecipes())
    }

    override suspend fun getRecipeDataById(id: Int): RecipeInformation {
        return convertRecipeInfoEntityToRecipeInformation(recipeInfoDAO.getFavoriteRecipeById(id))
    }

    override suspend fun removeRecipeFromData(id: Int) {
        recipeInfoDAO.deleteRecipeFromFavorite(id)
    }

}