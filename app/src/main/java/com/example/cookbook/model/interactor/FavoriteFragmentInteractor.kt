package com.example.cookbook.model.interactor

import com.example.cookbook.model.AppState
import com.example.cookbook.model.repository.local.LocalRepositoryInfoImpl

class FavoriteFragmentInteractor(
    private val localRepositoryInfoImpl: LocalRepositoryInfoImpl
) {
    suspend fun getRecipesFromDatabase():AppState{
        return AppState.Success(localRepositoryInfoImpl.getAllRecipesData())
    }
}