package com.example.cookbook.model.datasource

import com.example.cookbook.model.datasource.DTO.searchRecipe.SearchRecipeListDTO
import retrofit2.Response

interface SearchRecipeDataSource {
    suspend fun getSearchResult(
        request: String?,
        cuisine: String?,
        includeIngredients: String?,
        excludeIngredients: String?,
        userDiets: String?,
        userIntolerances: String?,
        dishType: String?,
        maxReadyTime: Int?,
        minCalories: Int?,
        maxCalories: Int?,
        currentPage: Int?
    ): Response<SearchRecipeListDTO>
}