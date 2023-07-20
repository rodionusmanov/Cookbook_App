package com.example.cookbook.viewModel.searchRecipe

import com.example.cookbook.domain.Recipe

sealed class SearchRecipeFragmentAppState {
    data class Success(val recipeList: List<Recipe>) : SearchRecipeFragmentAppState()
    data class Error(val error: Any) : SearchRecipeFragmentAppState()
    object Loading : SearchRecipeFragmentAppState()
}
