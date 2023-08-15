package com.example.cookbook.model.datasource

import com.example.cookbook.model.datasource.DTO.randomRecipe.RandomRecipeListDTO
import com.example.cookbook.model.datasource.DTO.searchRecipe.SearchRecipeListDTO
import retrofit2.Response

interface RandomRecipeDataSource {
    suspend fun getRandomRecipes(): Response<RandomRecipeListDTO>
    suspend fun getRandomRecipesByType(dishType: String): Response<SearchRecipeListDTO>
}