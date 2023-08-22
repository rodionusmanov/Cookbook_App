package com.example.cookbook.view.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.model.domain.RecipeInformation
import com.example.cookbook.model.interactor.FavoriteFragmentInteractor
import com.example.cookbook.model.repository.local.LocalRepositoryInfoImpl
import com.example.cookbook.view.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteRecipesViewModel(
    private val interactor: FavoriteFragmentInteractor,
    private val localRepository: LocalRepositoryInfoImpl
) : BaseViewModel<AppState>() {

    private val _stateFlow = MutableStateFlow<AppState>(AppState.Loading)
    val stateFlow: StateFlow<AppState> get() = _stateFlow.asStateFlow()

    /*fun getAllLocalRecipes(): LiveData<List<RecipeInformation>> {
        val result = MutableLiveData<List<RecipeInformation>>()
        viewModelCoroutineScope.launch {
            val returnedData = localRepository.getAllRecipesData()
            result.postValue(returnedData)
        }
        return result
    }*/

    fun getRecipesFromDatabase() {
        viewModelCoroutineScope.launch {
            _stateFlow.value = AppState.Loading
            try {
                _stateFlow.emit(interactor.getRecipesFromDatabase())
            } catch (e: Throwable) {
                _stateFlow.emit(AppState.Error(e))
            }
        }
    }

    /*fun insertNewRecipeToDataBase(recipeData: BaseRecipeData) {
        viewModelCoroutineScope.launch {
            localRepository.insertNewRecipe(recipeData)
        }
    }

    fun deleteRecipeFromData(id: Int) {
        viewModelCoroutineScope.launch {
            localRepository.removeRecipeFromData(id)
        }
    }*/
}