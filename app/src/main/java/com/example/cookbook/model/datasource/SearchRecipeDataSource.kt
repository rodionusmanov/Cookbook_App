package com.example.cookbook.model.datasource

import com.example.cookbook.model.data.searchRecipe.SearchRecipeListDTO
import retrofit2.Response

interface SearchRecipeDataSource {
    suspend fun getSearchResult(request: String, ingredients: String): Response<SearchRecipeListDTO>
}