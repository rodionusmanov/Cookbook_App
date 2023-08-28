package com.example.cookbook.view.favorite

import com.example.cookbook.model.AppState
import com.example.cookbook.model.interactor.FavoriteFragmentInteractor
import com.example.cookbook.model.repository.local.LocalRepositoryInfoImpl
import com.example.cookbook.model.room.fullRecipe.RecipeInfoEntity
import com.example.cookbook.view.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class FavoriteRecipesViewModel(
    private val interactor: FavoriteFragmentInteractor,
    private val localRepository: LocalRepositoryInfoImpl
) : BaseViewModel<AppState>() {

    private val _stateFlow = MutableStateFlow<Flow<List<RecipeInfoEntity>>>(flow { })
    val stateFlow: StateFlow<Flow<List<RecipeInfoEntity>>> get() = _stateFlow.asStateFlow()

    fun getRecipesFromDatabase() {
        viewModelCoroutineScope.launch {
            try {
                _stateFlow.emit(interactor.getRecipesFromDatabase())
            } catch (e: Throwable) {
            }
        }
    }

    fun upsertRecipeToDatabase(recipeInformation: RecipeInformation) {
        viewModelCoroutineScope.launch {
            localRepository.upsertNewRecipe(recipeInformation)
        }
    }

    fun deleteRecipeFromFavorite(id: Int) {
        viewModelCoroutineScope.launch {
            localRepository.removeRecipeFromData(id)
        }
    }
}