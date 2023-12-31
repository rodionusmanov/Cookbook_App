package com.example.cookbook.model.interactor

import com.example.cookbook.model.AppState
import com.example.cookbook.model.repository.local.LocalRepositoryInfoImpl
import com.example.cookbook.model.repository.remote.IRepositorySearchRequest
import com.example.cookbook.model.repository.sharedPreferences.SharedPreferencesRepository
import kotlinx.coroutines.flow.Flow

class SearchFragmentInteractor(
    private val remoteRepository: IRepositorySearchRequest,
    private val localRepository: LocalRepositoryInfoImpl,
    private val preferencesRepository: SharedPreferencesRepository
) {

    suspend fun searchRecipe(
        request: String?,
        cuisine: String?,
        includeIngredients: String?,
        excludeIngredients: String?,
        dishType: String?,
        maxReadyTime: Int?,
        minCalories: Int?,
        maxCalories: Int?,
        isOnline: Boolean,
        currentPage: Int?
    ): AppState {
        val userDiets = preferencesRepository.getSelectedDiets()
        val userIntolerances = preferencesRepository.getSelectedIntolerances()
        return try {
            val results = remoteRepository.getSearchResult(
                request,
                cuisine,
                includeIngredients,
                excludeIngredients,
                userDiets,
                userIntolerances,
                dishType,
                maxReadyTime,
                minCalories, maxCalories, currentPage
            )
            AppState.Success(results)
        } catch (e: Exception) {
            AppState.Error(e)
        }
    }

    fun observeRecipeExistence(id: Int): Flow<Boolean> {
        return localRepository.observeRecipeExistence(id)
    }
}