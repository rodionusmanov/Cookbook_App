package com.example.cookbook.viewModel.searchRecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.SearchRecipeData
import com.example.cookbook.model.repository.local.LocalRepositoryImpl
import com.example.cookbook.view.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchResultViewModel(
    private val localRepository: LocalRepositoryImpl
) : BaseViewModel<AppState>() {

    private val _stateFlow = MutableStateFlow<AppState>(AppState.Loading)
    val stateFlow: StateFlow<AppState> get() = _stateFlow

    fun getAllLocalRecipes(): LiveData<List<SearchRecipeData>> {
        val result = MutableLiveData<List<SearchRecipeData>>()
        viewModelCoroutineScope.launch {
            val returnedData = localRepository.getAllRecipesData()
            result.postValue(returnedData)
        }
        return result
    }

    fun insertNewRecipeToDataBase(recipeData: SearchRecipeData) {
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