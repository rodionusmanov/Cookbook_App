package com.example.cookbook.model.interactor

import com.example.cookbook.model.AppState
import com.example.cookbook.model.repository.local.LocalRepositoryInfoImpl

class RecipeFromDatabaseFragmentInteractor(
    private val localRepositoryInfoImpl: LocalRepositoryInfoImpl
) {
    suspend fun getRecipeFromDatabase(id: Int): AppState {
        return AppState.Success(localRepositoryInfoImpl.getRecipeDataById(id))
    }
}