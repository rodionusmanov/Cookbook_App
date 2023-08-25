package com.example.cookbook.model.interactor

import com.example.cookbook.model.AppState
import com.example.cookbook.model.repository.remote.IRepositorySearchRequest

class RecipeInfoFragmentInteractor(
    private val remoteRepository: IRepositorySearchRequest
) {
    suspend fun getRecipeInfo(id: Int, isOnline: Boolean): AppState {
        return AppState.Success(remoteRepository.getRecipeInfo(id))
    }
}