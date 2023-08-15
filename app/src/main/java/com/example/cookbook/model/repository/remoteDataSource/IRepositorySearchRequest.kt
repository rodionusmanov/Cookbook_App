package com.example.cookbook.model.repository.remoteDataSource

import com.example.cookbook.model.domain.RandomRecipeData
import com.example.cookbook.model.domain.RecipeInformation
import com.example.cookbook.model.domain.SearchRecipeData


interface IRepositorySearchRequest {
    suspend fun getSearchResult(request: String, ingredients: String): List<SearchRecipeData>
    suspend fun getRecipeInfo(id: Int): RecipeInformation
    suspend fun getRandomRecipes(): List<RandomRecipeData>
    suspend fun getRandomRecipesByType(dishType: String): List<SearchRecipeData>
}

