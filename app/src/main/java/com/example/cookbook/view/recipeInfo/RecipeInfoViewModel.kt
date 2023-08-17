package com.example.cookbook.view.recipeInfo

import android.util.Log
import com.example.cookbook.model.AppState
import com.example.cookbook.model.datasource.DTO.recipeInformation.AnalyzedInstruction
import com.example.cookbook.model.datasource.DTO.recipeInformation.ExtendedIngredient
import com.example.cookbook.model.interactor.RecipeInfoFragmentInteractor
import com.example.cookbook.view.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipeInfoViewModel(
    private val interactor: RecipeInfoFragmentInteractor
) : BaseViewModel<AppState>() {
    init {
        Log.d("RIF", "init")
    }

    private val _stateFlow = MutableStateFlow<AppState>(AppState.Loading)
    val stateFlow: StateFlow<AppState> get() = _stateFlow.asStateFlow()

    private val _ingredients = MutableStateFlow<List<ExtendedIngredient>>(listOf())
    val ingredients: StateFlow<List<ExtendedIngredient>> get() = _ingredients.asStateFlow()

    private val _instructions = MutableStateFlow<List<AnalyzedInstruction>>(listOf())
    val instructions: StateFlow<List<AnalyzedInstruction>> get() = _instructions.asStateFlow()

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

    fun setIngredients(list: List<ExtendedIngredient>) {
        viewModelCoroutineScope.launch {
            _ingredients.value = list
        }
    }

    fun setInstructions(list: List<AnalyzedInstruction>) {
        viewModelCoroutineScope.launch {
            _instructions.value = list
        }
    }
}