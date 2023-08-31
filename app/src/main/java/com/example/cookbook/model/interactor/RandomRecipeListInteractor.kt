package com.example.cookbook.model.interactor

import com.example.cookbook.model.AppState
import com.example.cookbook.model.repository.local.LocalRepositoryInfoImpl
import com.example.cookbook.model.repository.remote.IRepositorySearchRequest
import com.example.cookbook.model.repository.sharedPreferences.SharedPreferencesRepository

class RandomRecipeListInteractor(
    private val remoteRepository: IRepositorySearchRequest,
    private val localRepository: LocalRepositoryInfoImpl,
    private val preferencesRepository: SharedPreferencesRepository
) {

    suspend fun getRandomRecipes(): AppState {
        val userDiets = preferencesRepository.getSelectedDiets()
        val userIntolerances = preferencesRepository.getSelectedIntolerances()
        return AppState.Success(remoteRepository.getRandomRecipes(userDiets, userIntolerances))
    }

    suspend fun getRandomRecipesByCuisine(cuisine: String): AppState {
        val userIntolerances = preferencesRepository.getSelectedIntolerances()
        return AppState.Success(remoteRepository.getRandomCuisineRecipes(cuisine, userIntolerances))
    }

    suspend fun getHealthyRandomRecipes(): AppState {
        return AppState.Success(remoteRepository.getHealthyRandomRecipes())
    }

    suspend fun checkRecipeExistenceInDatabase(id: Int): Boolean {
       return localRepository.checkExistence(id)
    }
}