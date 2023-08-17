package com.example.cookbook.view.home.randomCuisineRecipes

import com.example.cookbook.model.AppState
import com.example.cookbook.model.interactor.RandomRecipeListInteractor
import com.example.cookbook.view.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RandomCuisineRecipeListViewModel(
    private val interactor: RandomRecipeListInteractor
) : BaseViewModel<AppState>() {

    private val _stateFlow = MutableStateFlow<AppState>(AppState.Loading)
    val stateFlow: StateFlow<AppState> get() = _stateFlow

    /*fun insertNewRecipeToDataBase(recipeData: BaseRecipeData) {
        viewModelCoroutineScope.launch {
            interactor.insertRecipeToDataBase(recipeData)
        }
    }

    fun deleteRecipeFromData(id: Int) {
        viewModelCoroutineScope.launch {
            interactor.deleteRecipeFromDataBase(id)
        }
    }*/

    fun getRandomRecipesByCuisine(cuisine: String) {
        viewModelCoroutineScope.launch {
            _stateFlow.value = AppState.Loading
            try {
                _stateFlow.emit(interactor.getRandomRecipesByCuisine(cuisine))
            } catch (e: Throwable) {
                _stateFlow.emit(AppState.Error(e))
            }
        }
    }
}