package com.example.cookbook.model.interactor

import com.example.cookbook.model.repository.local.LocalRepositoryInfoImpl
import com.example.cookbook.model.room.RecipeInfoEntity
import kotlinx.coroutines.flow.Flow

class FavoriteFragmentInteractor(
    private val localRepositoryInfoImpl: LocalRepositoryInfoImpl
) {

    fun getRecipesFromDatabase(): Flow<List<RecipeInfoEntity>> {
        return localRepositoryInfoImpl.getAllRecipesData()
    }
}