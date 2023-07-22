package com.example.cookbook.viewModel.searchRecipe

import com.example.cookbook.model.AppState
import com.example.cookbook.model.interactor.SearchFragmentInteractor
import com.example.cookbook.view.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchRecipeViewModel(
    private val interactor: SearchFragmentInteractor
) : BaseViewModel<AppState>() {

    private val _stateFlow = MutableStateFlow<AppState>(AppState.Loading)
    val stateFlow: StateFlow<AppState> get() = _stateFlow

    fun searchRecipeRequest(request: String) {
        viewModelCoroutineScope.launch {
            _stateFlow.value = AppState.Loading
            try {
                _stateFlow.emit(interactor.searchRecipe(request, true))
            } catch (e:Throwable) {
                _stateFlow.emit(AppState.Error(e))
            }
        }
    }
}