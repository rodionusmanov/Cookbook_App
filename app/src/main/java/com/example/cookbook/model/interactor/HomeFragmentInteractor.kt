package com.example.cookbook.model.interactor

import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.model.repository.local.LocalRepositoryImpl
import com.example.cookbook.model.repository.remoteDataSource.IRepositorySearchRequest

class HomeFragmentInteractor(
    private val remoteRepository: IRepositorySearchRequest,
    private val localRepository: LocalRepositoryImpl
) {

    suspend fun searchRecipe(request: String, ingredients: String, isOnline: Boolean): AppState {
        return AppState.Success(remoteRepository.getSearchResult(request, ingredients))
    }

    suspend fun getRandomRecipes(): AppState {
        return AppState.Success(remoteRepository.getRandomRecipes())
    }

    suspend fun insertRecipeToDataBase(recipeData: BaseRecipeData) {
        localRepository.insertNewRecipe(recipeData)
    }

    suspend fun deleteRecipeFromDataBase(id: Int) {
        localRepository.removeRecipeFromData(id)
    }

    suspend fun getAllRecipesFromDataBase(): List<BaseRecipeData> {
        return localRepository.getAllRecipesData()
    }
}