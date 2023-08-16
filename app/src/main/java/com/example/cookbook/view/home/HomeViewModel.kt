package com.example.cookbook.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.model.interactor.HomeFragmentInteractor
import com.example.cookbook.view.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val interactor: HomeFragmentInteractor
) : BaseViewModel<AppState>() {

    private val _stateFlow = MutableStateFlow<AppState>(AppState.Loading)
    val stateFlow: StateFlow<AppState> get() = _stateFlow

    fun getJokeText(){
        viewModelCoroutineScope.launch {
            _stateFlow.value = AppState.Loading
            try {
                _stateFlow.emit(interactor.getJokeText())
            } catch (e: Throwable) {
                _stateFlow.emit(AppState.Error(e))
            }
        }
    }

    /*fun getAllLocalRecipes(): LiveData<List<BaseRecipeData>> {
        val result = MutableLiveData<List<BaseRecipeData>>()
        viewModelCoroutineScope.launch {
            val returnedData = interactor.getAllRecipesFromDataBase()
            result.postValue(returnedData)
        }
        return result
    }

    fun insertNewRecipeToDataBase(recipeData: BaseRecipeData) {
        viewModelCoroutineScope.launch {
            interactor.insertRecipeToDataBase(recipeData)
        }
    }

    fun deleteRecipeFromDataBase(id: Int) {
        viewModelCoroutineScope.launch {
            interactor.deleteRecipeFromDataBase(id)
        }
    }*/
}