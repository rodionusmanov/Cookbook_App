package com.example.cookbook.view.search

import android.os.Bundle
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.model.interactor.SearchFragmentInteractor
import com.example.cookbook.utils.DEFAULT_CUISINE
import com.example.cookbook.utils.DEFAULT_EXCLUDE_INGREDIENTS
import com.example.cookbook.utils.DEFAULT_INCLUDE_INGREDIENTS
import com.example.cookbook.utils.DEFAULT_MAX_CALORIES
import com.example.cookbook.utils.DEFAULT_MIN_CALORIES
import com.example.cookbook.utils.DEFAULT_QUERY
import com.example.cookbook.utils.DEFAULT_READY_TIME
import com.example.cookbook.utils.DEFAULT_TYPE
import com.example.cookbook.view.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val interactor: SearchFragmentInteractor
) : BaseViewModel<AppState>() {

    private val _stateFlow = MutableStateFlow<AppState>(AppState.Loading)
    val stateFlow: StateFlow<AppState> get() = _stateFlow.asStateFlow()

    private val _argumentsFlow = MutableStateFlow<Bundle?>(null)
    val argumentsFlow: StateFlow<Bundle?> get() = _argumentsFlow
    var isInitialLoad = true

    private var currentPage = 1

    private var lastRequest: String? = null
    private var lastCuisine: String? = null
    private var lastIncludeIngredients: String? = null
    private var lastExcludeIngredients: String? = null
    private var lastDishType: String? = null
    private var lastMaxReadyTime: Int? = null
    private var lastMinCalories: Int? = null
    private var lastMaxCalories: Int? = null
    fun searchRecipeRequest(
        request: String,
        cuisine: String,
        includeIngredients: String,
        excludeIngredients: String,
        dishType: String,
        maxReadyTime: Int,
        minCalories: Int,
        maxCalories: Int,
        loadNext: Boolean = false
    ) {

        if (loadNext) {
            currentPage++
            isInitialLoad = false
        } else {
            currentPage = 1
        }

        lastRequest = request
        lastCuisine = cuisine
        lastIncludeIngredients = includeIngredients
        lastExcludeIngredients = excludeIngredients
        lastDishType = dishType
        lastMaxReadyTime = maxReadyTime
        lastMinCalories = minCalories
        lastMaxCalories = maxCalories

        viewModelCoroutineScope.launch {
            _stateFlow.value = AppState.Loading

            try {
                val response = interactor.searchRecipe(
                    request,
                    cuisine,
                    includeIngredients,
                    excludeIngredients,
                    dishType,
                    maxReadyTime,
                    minCalories,
                    maxCalories,
                    true,
                    currentPage
                )

                when (response) {
                    is AppState.Success<*> -> {
                        val newRecipes: List<BaseRecipeData> = if (response.data is List<*>) {
                            (response.data as? List<*>)?.filterIsInstance<BaseRecipeData>()
                                ?: listOf()
                        } else {
                            listOf()
                        }

                        if (loadNext) {
                            val currentRecipes = when (val currentState = _stateFlow.value) {
                                is AppState.Success<*> -> {
                                    (currentState.data as? List<*>)?.filterIsInstance<BaseRecipeData>()
                                        ?: listOf()
                                }

                                else -> listOf()
                            }
                            val combinedRecipes = currentRecipes + newRecipes
                            _stateFlow.emit(AppState.Success(combinedRecipes))
                        } else {
                            _stateFlow.emit(AppState.Success(newRecipes))
                        }
                    }

                    is AppState.Error -> {
                        _stateFlow.emit(AppState.Error(response.error))
                    }

                    is AppState.Loading -> {}
                }
            } catch (e: Throwable) {
                _stateFlow.emit(AppState.Error(e))
            }
        }
    }

    fun updateArguments(arguments: Bundle) {
        viewModelCoroutineScope.launch {
            _argumentsFlow.emit(arguments)
        }
    }

    fun loadNextPage() {
        searchRecipeRequest(
            lastRequest ?: DEFAULT_QUERY,
            lastCuisine ?: DEFAULT_CUISINE,
            lastIncludeIngredients ?: DEFAULT_INCLUDE_INGREDIENTS,
            lastExcludeIngredients ?: DEFAULT_EXCLUDE_INGREDIENTS,
            lastDishType ?: DEFAULT_TYPE,
            lastMaxReadyTime ?: DEFAULT_READY_TIME,
            lastMinCalories ?: DEFAULT_MIN_CALORIES,
            lastMaxCalories ?: DEFAULT_MAX_CALORIES, true
        )
    }
}