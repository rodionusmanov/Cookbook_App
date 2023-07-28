package com.example.cookbook.model.datasource

import com.example.cookbook.model.data.recipeInformation.RecipeInformationDTO
import retrofit2.Response

interface RecipeInformationDataSource {
    suspend fun getRecipeFullInformation(id: Int): Response<RecipeInformationDTO>
}