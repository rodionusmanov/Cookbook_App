package com.example.cookbook.view.home.randomCuisineRecipes

import com.example.cookbook.model.AppState
import com.example.cookbook.model.interactor.RandomRecipeListInteractor
import com.example.cookbook.view.base.BaseViewModel
import com.example.cookbook.view.home.randomRecipe.CheckRecipeExistenceViewModelExistence
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RandomCuisineRecipeListViewModel(
    private val interactor: RandomRecipeListInteractor
) : BaseViewModel<AppState>(), CheckRecipeExistenceViewModelExistence {

    private val _stateFlow = MutableStateFlow<AppState>(AppState.Loading)
    val stateFlow: StateFlow<AppState> get() = _stateFlow

    private val _recipeExistenceInDatabase = MutableStateFlow<Pair<Int, Boolean>?>(null)
    override val recipeExistenceInDatabase: StateFlow<Pair<Int, Boolean>?> get() =
        _recipeExistenceInDatabase.asStateFlow()

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

    override fun observeRecipeExistenceInDatabase(id: Int) {
        viewModelCoroutineScope.launch {
            interactor.observeRecipeExistence(id).collect {exists ->
                _recipeExistenceInDatabase.value = id to exists
            }
        }
    }
}