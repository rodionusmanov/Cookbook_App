package com.example.cookbook.model.interactor

import com.example.cookbook.model.AppState
import com.example.cookbook.model.repository.remote.IRepositorySearchRequest
import com.example.cookbook.model.repository.sharedPreferences.SharedPreferencesRepository

class SearchFragmentInteractor(
    private val remoteRepository: IRepositorySearchRequest,
    private val preferencesRepository: SharedPreferencesRepository
) {

    suspend fun searchRecipe(
        request: String,
        cuisine: String,
        includeIngredients: String,
        excludeIngredients: String,
        dishType: String,
        maxReadyTime: Int,
        minCalories: Int,
        maxCalories: Int,
        isOnline: Boolean
    ): AppState {
        val userDiets = preferencesRepository.getSelectedDiets()
        val userIntolerances = preferencesRepository.getSelectedIntolerances()
        return AppState.Success(
            remoteRepository.getSearchResult(
                request,
                cuisine,
                includeIngredients,
                excludeIngredients,
                userDiets,
                userIntolerances,
                dishType,
                maxReadyTime,
                minCalories, maxCalories
            )
        )
    }
}