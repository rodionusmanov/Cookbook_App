package com.example.cookbook.viewModel.searchRecipe

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cookbook.domain.Recipe
import com.example.cookbook.model.AppState
import com.example.cookbook.model.IRepositorySearchRequestToRecipeList
import com.example.cookbook.model.ISearchRecipeCallback
import java.io.IOException

class SearchRecipeViewModel(
    val searchRecipeLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: IRepositorySearchRequestToRecipeList
) : ViewModel() {

    fun searchRecipeRequest(request: String) {
//        searchRecipeLiveData.value = SearchRecipeFragmentAppState.Loading
        repository.getSearchResult(request, callback)
    }

    private val callback = object : ISearchRecipeCallback {
        override fun onResponse(recipeList: List<Recipe>) {
            searchRecipeLiveData.postValue(AppState.Success(recipeList))
        }

        override fun onFailure(e: IOException) {
            searchRecipeLiveData.postValue(AppState.Error(e))
        }
    }
}