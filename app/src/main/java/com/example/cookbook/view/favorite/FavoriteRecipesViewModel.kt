package com.example.cookbook.view.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.model.repository.local.LocalRepositoryImpl
import com.example.cookbook.view.base.BaseViewModel
import kotlinx.coroutines.launch

class FavoriteRecipesViewModel(
    private val localRepository: LocalRepositoryImpl
) : BaseViewModel<AppState>() {

    fun getAllLocalRecipes(): LiveData<List<BaseRecipeData>> {
        val result = MutableLiveData<List<BaseRecipeData>>()
        viewModelCoroutineScope.launch {
            val returnedData = localRepository.getAllRecipesData()
            result.postValue(returnedData)
        }
        return result
    }

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