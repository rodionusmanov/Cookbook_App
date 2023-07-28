package com.example.cookbook.model.repository.remoteDataSource

import com.example.cookbook.model.data.searchRecipe.SearchRecipeData


interface IRepositorySearchRequest {
    suspend fun getSearchResult(request: String, ingredients: String) : List<SearchRecipeData>
}

