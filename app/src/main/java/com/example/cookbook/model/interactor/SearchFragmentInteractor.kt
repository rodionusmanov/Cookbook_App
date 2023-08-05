package com.example.cookbook.model.interactor

import com.example.cookbook.model.AppState
import com.example.cookbook.model.repository.remoteDataSource.IRepositorySearchRequest

class SearchFragmentInteractor(
    private val remoteRepository: IRepositorySearchRequest
) {

    suspend fun searchRecipe(request:String, ingredients: String, isOnline: Boolean): AppState {
        return AppState.Success(remoteRepository.getSearchResult(request, ingredients))
    }

    suspend fun getRandomRecipes(): AppState {
        return AppState.Success(remoteRepository.getRandomRecipes())
    }

}