package com.example.cookbook.view.recipeInfo

import com.example.cookbook.model.AppState
import com.example.cookbook.model.interactor.RecipeInfoFragmentInteractor
import com.example.cookbook.view.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeInfoViewModel(
    private val interactor: RecipeInfoFragmentInteractor
) : BaseViewModel<AppState>() {

    private val _stateFlow = MutableStateFlow<AppState>(AppState.Loading)
    val stateFlow: StateFlow<AppState> get() = _stateFlow

    fun recipeInfoRequest(id: Int) {
        viewModelCoroutineScope.launch {
            _stateFlow.value = AppState.Loading
            try {
                _stateFlow.emit(interactor.getRecipeInfo(id, true))
            } catch (e: Throwable) {
                _stateFlow.emit(AppState.Error(e))
            }
        }
    }
}