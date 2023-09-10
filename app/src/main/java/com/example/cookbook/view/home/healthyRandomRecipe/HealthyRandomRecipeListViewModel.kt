package com.example.cookbook.view.home.healthyRandomRecipe

import com.example.cookbook.model.AppState
import com.example.cookbook.model.interactor.RandomRecipeListInteractor
import com.example.cookbook.view.base.BaseViewModel
import com.example.cookbook.view.home.randomRecipe.CheckRecipeExistence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class HealthyRandomRecipeListViewModel(
    private val interactor: RandomRecipeListInteractor
) : BaseViewModel<AppState>(), CheckRecipeExistence {

    private val _stateFlow = MutableStateFlow<AppState>(AppState.Loading)
    val stateFlow: StateFlow<AppState> get() = _stateFlow

    private val _recipeExistenceInDatabase =
        MutableSharedFlow<Pair<Int, Boolean>?>(replay = 0, extraBufferCapacity = 100)
    override val recipeExistenceInDatabase: Flow<Pair<Int, Boolean>?>
        get() = _recipeExistenceInDatabase.asSharedFlow()

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

    fun getRandomRecipes() {
        viewModelCoroutineScope.launch {
            _stateFlow.value = AppState.Loading
            try {
                _stateFlow.emit(interactor.getHealthyRandomRecipes())
            } catch (e: Throwable) {
                _stateFlow.emit(AppState.Error(e))
            }
        }
    }

    override fun setRecipeIdsToWatch(ids: List<Int>) {
        recipeIdsToWatch.value = ids
    }
}
