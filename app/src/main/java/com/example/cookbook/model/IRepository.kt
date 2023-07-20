package com.example.cookbook.model

import com.example.cookbook.domain.Recipe
import java.io.IOException

interface IRepositorySearchRequestToRecipeList {
    fun getSearchResult(request: String, callback: ISearchRecipeCallback)
}

interface ISearchRecipeCallback {
    fun onResponse(recipeList: List<Recipe>)
    fun onFailure(e: IOException)
}
