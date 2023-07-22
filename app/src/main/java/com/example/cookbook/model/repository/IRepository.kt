package com.example.cookbook.model.repository

import com.example.cookbook.domain.Recipe

interface IRepositorySearchRequestToRecipeList {
    suspend fun getSearchResult(request: String) : List<Recipe>
}
