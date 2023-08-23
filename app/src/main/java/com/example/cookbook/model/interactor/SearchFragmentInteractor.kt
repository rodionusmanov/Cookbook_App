package com.example.cookbook.model.interactor

import com.example.cookbook.model.AppState
import com.example.cookbook.model.repository.remoteDataSource.IRepositorySearchRequest
import com.example.cookbook.model.repository.sharedPreferences.SharedPreferencesRepository

class SearchFragmentInteractor(
    private val remoteRepository: IRepositorySearchRequest,
    private val preferencesRepository: SharedPreferencesRepository
) {

    suspend fun searchRecipe(request: String, ingredients: String, isOnline: Boolean): AppState {
        val userDiets = preferencesRepository.getSelectedDiets()
        val userIntolerances = preferencesRepository.getSelectedIntolerances()
        return AppState.Success(
            remoteRepository.getSearchResult(
                request,
                ingredients,
                userDiets,
                userIntolerances
            )
        )
    }

    suspend fun searchRecipesByDishTypes(dishType: String, isOnline: Boolean): AppState {
        val userDiets = preferencesRepository.getSelectedDiets()
        val userIntolerances = preferencesRepository.getSelectedIntolerances()
        return AppState.Success(
            remoteRepository.getRecipesByType(
                dishType,
                userDiets,
                userIntolerances
            )
        )
    }
}