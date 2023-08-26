package com.example.cookbook.view.home.healthyRandomRecipe

import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.model.domain.RecipeInformation
import com.example.cookbook.model.interactor.RandomRecipeListInteractor
import com.example.cookbook.view.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HealthyRandomRecipeListViewModel(
    private val interactor: RandomRecipeListInteractor
) : BaseViewModel<AppState>() {

    private val _stateFlow = MutableStateFlow<AppState>(AppState.Loading)
    val stateFlow: StateFlow<AppState> get() = _stateFlow

    fun getRandomRecipes() {
        viewModelCoroutineScope.launch {
            _stateFlow.value = AppState.Loading
            try {
                _stateFlow.emit(interactor.getHealthyRandomRecipes())
            } catch (e: Throwable) {
                _stateFlow.emit(AppState.Error(e))
            }
        }
    }
}
