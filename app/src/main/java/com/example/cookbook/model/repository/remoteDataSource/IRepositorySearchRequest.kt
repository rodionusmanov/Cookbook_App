package com.example.cookbook.model.repository.remoteDataSource

import com.example.cookbook.model.domain.RandomRecipeData
import com.example.cookbook.model.domain.RecipeInformation
import com.example.cookbook.model.domain.SearchRecipeData


interface IRepositorySearchRequest {
    suspend fun getSearchResult(
        request: String,
        ingredients: String,
        userDiets: String,
        userIntolerances: String
    ): List<SearchRecipeData>
    suspend fun getRecipeInfo(id: Int): RecipeInformation
    suspend fun getRandomRecipes(
        userDiets: String,
        userIntolerances: String
    ): List<RandomRecipeData>
    suspend fun getRandomCuisineRecipes(
        cuisine: String,
        userIntolerances: String
    ): List<RandomRecipeData>
    suspend fun getRecipesByType(
        dishType: String,
        userDiets: String,
        userIntolerances: String
    ): List<SearchRecipeData>
    suspend fun getHealthyRandomRecipes(): List<RandomRecipeData>
    suspend fun getJokeText(): String
}

