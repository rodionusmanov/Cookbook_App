package com.example.cookbook.model.interactor

import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.model.domain.RecipeInformation
import com.example.cookbook.model.repository.local.LocalRepositoryImpl
import com.example.cookbook.model.repository.local.LocalRepositoryInfoImpl
import com.example.cookbook.model.repository.remoteDataSource.IRepositorySearchRequest

class HomeFragmentInteractor(
    private val remoteRepository: IRepositorySearchRequest,
    private val localRepository: LocalRepositoryInfoImpl
) {

    suspend fun insertRecipeToDataBase(recipeData: BaseRecipeData) {
        localRepository.insertNewRecipe(recipeData)
    }

    suspend fun deleteRecipeFromDataBase(id: Int) {
        localRepository.removeRecipeFromData(id)
    }

    suspend fun getAllRecipesFromDataBase(): List<BaseRecipeData> {
        return localRepository.getAllRecipesData()
    }

    suspend fun getJokeText(): AppState {
        return AppState.Success(remoteRepository.getJokeText())
    }
}