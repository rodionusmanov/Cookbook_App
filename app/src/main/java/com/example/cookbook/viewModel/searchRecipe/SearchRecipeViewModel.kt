package com.example.cookbook.viewModel.searchRecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.SearchRecipeData
import com.example.cookbook.model.interactor.SearchFragmentInteractor
import com.example.cookbook.model.repository.local.LocalRepositoryImpl
import com.example.cookbook.view.base.BaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchRecipeViewModel(
    private val interactor: SearchFragmentInteractor,
    private val localRepository: LocalRepositoryImpl
) : BaseViewModel<AppState>() {

    private val _stateFlow = MutableStateFlow<AppState>(AppState.Loading)
    val stateFlow: StateFlow<AppState> get() = _stateFlow

    fun searchRecipeRequest(request: String, ingredients: String) {
        viewModelCoroutineScope.launch {
            _stateFlow.value = AppState.Loading
            try {
                _stateFlow.emit(interactor.searchRecipe(request, ingredients, true))
            } catch (e: Throwable) {
                _stateFlow.emit(AppState.Error(e))
            }
        }
    }

    fun getRandomRecipes(){
        viewModelCoroutineScope.launch {
            _stateFlow.value = AppState.Loading
            try {
                _stateFlow.emit(interactor.getRandomRecipes())
            } catch (e:Throwable) {
                _stateFlow.emit(AppState.Error(e))
            }
        }
    }

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