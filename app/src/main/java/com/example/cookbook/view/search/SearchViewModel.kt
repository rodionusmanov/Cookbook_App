package com.example.cookbook.view.search

import android.os.Bundle
import com.example.cookbook.model.AppState
import com.example.cookbook.model.interactor.SearchFragmentInteractor
import com.example.cookbook.view.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val interactor: SearchFragmentInteractor
) : BaseViewModel<AppState>() {

    private val _stateFlow = MutableStateFlow<AppState>(AppState.Loading)
    val stateFlow: StateFlow<AppState> get() = _stateFlow.asStateFlow()

    private val _argumentsFlow = MutableStateFlow<Bundle?>(null)
    val argumentsFlow: StateFlow<Bundle?> get() = _argumentsFlow
    fun searchRecipeRequest(
        request: String,
        cuisine: String,
        includeIngredients: String,
        excludeIngredients: String,
        dishType: String,
        maxReadyTime: Int,
        minCalories: Int,
        maxCalories: Int
    ) {
        viewModelCoroutineScope.launch {
            _stateFlow.value = AppState.Loading
            try {
                _stateFlow.emit(interactor.searchRecipe(
                    request,
                    cuisine,
                    includeIngredients,
                    excludeIngredients,
                    dishType,
                    maxReadyTime,
                    minCalories,
                    maxCalories, true))
            } catch (e: Throwable) {
                _stateFlow.emit(AppState.Error(e))
            }
        }
    }

    fun updateArguments(arguments: Bundle) {
        viewModelCoroutineScope.launch {
            _argumentsFlow.emit(arguments)
        }
    }
}