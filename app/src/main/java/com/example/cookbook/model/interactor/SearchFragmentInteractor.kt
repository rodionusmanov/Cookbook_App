package com.example.cookbook.model.interactor

import com.example.cookbook.model.AppState
import com.example.cookbook.model.repository.IRepositorySearchRequestToRecipeList

class SearchFragmentInteractor(
    private val remoteRepository: IRepositorySearchRequestToRecipeList
) {

    suspend fun searchRecipe(request:String, ingredients: String, isOnline: Boolean): AppState {
        return AppState.Success(remoteRepository.getSearchResult(request, ingredients))
    }
}