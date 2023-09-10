package com.example.cookbook.view.search

import android.os.Bundle
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.model.interactor.SearchFragmentInteractor
import com.example.cookbook.view.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel(
    private val interactor: SearchFragmentInteractor
) : BaseViewModel<AppState>() {

    private val _stateFlow = MutableStateFlow<AppState>(AppState.Loading)
    val stateFlow: StateFlow<AppState> get() = _stateFlow.asStateFlow()

    private val _argumentsFlow = MutableStateFlow<Bundle?>(null)
    val argumentsFlow: StateFlow<Bundle?> get() = _argumentsFlow
    var isInitialLoad = true

    private val allRecipes = mutableListOf<BaseRecipeData>()

    private var currentPage = 1

    private val _recipeExistenceInDatabase =
        MutableSharedFlow<Pair<Int, Boolean>?>(replay = 0, extraBufferCapacity = 100)
    val recipeExistenceInDatabase: Flow<Pair<Int, Boolean>?> get() =
        _recipeExistenceInDatabase.asSharedFlow()

    private val recipeIdsToWatch = MutableStateFlow<List<Int>>(emptyList())

    init {
        viewModelCoroutineScope.launch {
            recipeIdsToWatch.flatMapLatest { ids ->
                combine(ids.map {id -> interactor.observeRecipeExistence(id)}) { existences ->
                    existences.mapIndexed {index, exists -> ids[index] to exists}
                }
            }.collect { pairs ->
                pairs.forEach { pair ->
                    _recipeExistenceInDatabase.emit(pair)
                }
            }
        }
    }

    private var lastRequest: String? = null
    private var lastCuisine: String? = null
    private var lastIncludeIngredients: String? = null
    private var lastExcludeIngredients: String? = null
    private var lastDishType: String? = null
    private var lastMaxReadyTime: Int? = null
    private var lastMinCalories: Int? = null
    private var lastMaxCalories: Int? = null
    fun searchRecipeRequest(
        request: String? = null,
        cuisine: String? = null,
        includeIngredients: String? = null,
        excludeIngredients: String? = null,
        dishType: String? = null,
        maxReadyTime: Int? = null,
        minCalories: Int? = null,
        maxCalories: Int? = null,
        loadNext: Boolean = false
    ) {

        if (loadNext) {
            currentPage++
            isInitialLoad = false
        } else {
            currentPage = 1
            allRecipes.clear()
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
                processResponse(response, loadNext)
            } catch (e: Throwable) {
                _stateFlow.emit(AppState.Error(e))
            }
        }
    }

    private suspend fun processResponse(response: AppState, loadNext: Boolean) {
        when (response) {
            is AppState.Success<*> -> {
                val newRecipes = (response.data as? List<*>)?.filterIsInstance<BaseRecipeData>()
                    ?: listOf()

                if (loadNext) {
                    allRecipes.addAll(newRecipes)
                } else {
                    allRecipes.clear()
                    allRecipes.addAll(newRecipes)
                }
                _stateFlow.emit(AppState.Success(allRecipes.toList()))
            }

            is AppState.Error -> {
                _stateFlow.emit(AppState.Error(response.error))
            }

            is AppState.Loading -> {}
        }
    }

    fun updateArguments(arguments: Bundle) {
        viewModelCoroutineScope.launch {
            _argumentsFlow.emit(arguments)
        }
    }

    fun loadNextPage() {
        searchRecipeRequest(
            lastRequest,
            lastCuisine,
            lastIncludeIngredients,
            lastExcludeIngredients,
            lastDishType,
            lastMaxReadyTime,
            lastMinCalories,
            lastMaxCalories,
            true
        )
    }

    fun setRecipeIdsToWatch(ids: List<Int>) {
        recipeIdsToWatch.value = ids
    }
}
