package com.example.cookbook.model.repository.remoteDataSource

import com.example.cookbook.model.domain.SearchRecipeData


interface IRepositorySearchRequest {
    suspend fun getSearchResult(request: String, ingredients: String) : List<SearchRecipeData>
}

