package com.example.cookbook.model.interactor

import com.example.cookbook.model.AppState
import com.example.cookbook.model.repository.local.LocalRepositoryInfoImpl
import com.example.cookbook.model.repository.remoteDataSource.IRepositorySearchRequest

class RandomRecipeListInteractor(
    private val remoteRepository: IRepositorySearchRequest,
    private val localRepository: LocalRepositoryInfoImpl
) {

    suspend fun getRandomRecipes(): AppState {
        return AppState.Success(remoteRepository.getRandomRecipes())
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
    }*/
}