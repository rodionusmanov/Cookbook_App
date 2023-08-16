package com.example.cookbook.model.interactor

import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.model.repository.local.LocalRepositoryImpl
import com.example.cookbook.model.repository.remoteDataSource.IRepositorySearchRequest

class RandomRecipeListInteractor(
    private val remoteRepository: IRepositorySearchRequest,
    private val localRepository: LocalRepositoryImpl
) {

    suspend fun getRandomRecipes(): AppState {
        return AppState.Success(remoteRepository.getRandomRecipes())
    }

    suspend fun getRandomRecipesByCuisine(cuisine: String): AppState {
        return AppState.Success(remoteRepository.getRandomCuisineRecipes(cuisine))
    }

    suspend fun getHealthyRandomRecipes(): AppState {
        return AppState.Success(remoteRepository.getHealthyRandomRecipes())
    }

    suspend fun insertRecipeToDataBase(recipeData: BaseRecipeData){
        localRepository.insertNewRecipe(recipeData)
    }

    suspend fun deleteRecipeFromDataBase(id: Int) {
        localRepository.removeRecipeFromData(id)
    }

    suspend fun getAllRecipesFromDataBase(): List<BaseRecipeData> {
        return localRepository.getAllRecipesData()
    }
}