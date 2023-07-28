package com.example.cookbook.model.datasource

import com.example.cookbook.model.data.randomRecipe.RandomRecipeDTO
import retrofit2.Response

interface RandomRecipeDataSource {
    suspend fun getRandomRecipes(): Response<RandomRecipeDTO>
}