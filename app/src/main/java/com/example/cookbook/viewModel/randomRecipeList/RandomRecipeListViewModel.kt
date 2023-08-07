package com.example.cookbook.viewModel.randomRecipeList

import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.model.repository.local.LocalRepositoryImpl
import com.example.cookbook.view.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RandomRecipeListViewModel(
    private val localRepository: LocalRepositoryImpl
) : BaseViewModel<AppState>() {

    private val _stateFlow = MutableStateFlow<AppState>(AppState.Loading)
    val stateFlow: StateFlow<AppState> get() = _stateFlow

    fun insertNewRecipeToDataBase(recipeData: BaseRecipeData) {
        viewModelCoroutineScope.launch {
            localRepository.insertNewRecipe(recipeData)
        }
    }

    fun deleteRecipeFromData(id: Int) {
        viewModelCoroutineScope.launch {
            localRepository.removeRecipeFromData(id)
        }
    }

}