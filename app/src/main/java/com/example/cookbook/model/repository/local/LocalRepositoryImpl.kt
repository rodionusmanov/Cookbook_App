package com.example.cookbook.model.repository.local

import com.example.cookbook.model.domain.SearchRecipeData
import com.example.cookbook.model.room.IRecipesDAO
import com.example.cookbook.utils.convertRecipeEntityToList
import com.example.cookbook.utils.convertRecipeToEntity

class LocalRepositoryImpl(private val recipeDAO: IRecipesDAO): ILocalRecipesRepository {

    override suspend fun insertNewRecipe(recipeData: SearchRecipeData) {
        recipeDAO.insertNewRecipe(convertRecipeToEntity(recipeData))
    }

    override suspend fun getAllRecipesData(): List<SearchRecipeData> {
        return convertRecipeEntityToList(recipeDAO.getAllFavoriteRecipes())
    }

    override suspend fun removeRecipeFromData(id: Int) {
        recipeDAO.deleteRecipeFromFavorite(id)
    }
}