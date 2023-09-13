package com.example.cookbook.model.datasource

import com.example.cookbook.model.datasource.DTO.randomRecipe.RandomRecipeListDTO
import retrofit2.Response

interface RandomRecipeDataSource {
    suspend fun getRandomRecipes(
        userDiets: String,
        userIntolerances: String
    ): Response<RandomRecipeListDTO>

    suspend fun getHealthyRandomRecipes(): Response<RandomRecipeListDTO>

    suspend fun getRandomCuisineRecipes(
        cuisine: String,
        userIntolerances: String
    ): Response<RandomRecipeListDTO>

}