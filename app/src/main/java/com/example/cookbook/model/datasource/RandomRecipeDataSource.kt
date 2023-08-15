package com.example.cookbook.model.datasource

import com.example.cookbook.model.datasource.DTO.randomRecipe.RandomRecipeListDTO
import retrofit2.Response

interface RandomRecipeDataSource {
    suspend fun getRandomRecipes(): Response<RandomRecipeListDTO>

}