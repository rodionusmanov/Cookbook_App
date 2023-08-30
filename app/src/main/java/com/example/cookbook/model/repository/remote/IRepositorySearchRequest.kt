package com.example.cookbook.model.repository.remote

import com.example.cookbook.model.domain.RandomRecipeData
import com.example.cookbook.model.domain.RecipeInformation
import com.example.cookbook.model.domain.SearchRecipeData


interface IRepositorySearchRequest {
    suspend fun getSearchResult(
        request: String,
        cuisine: String,
        includeIngredients: String,
        excludeIngredients: String,
        userDiets: String,
        userIntolerances: String,
        dishType: String,
        maxReadyTime: Int,
        minCalories: Int,
        maxCalories: Int,
        currentPage: Int
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

    suspend fun getHealthyRandomRecipes(): List<RandomRecipeData>
    suspend fun getJokeText(): String
}

