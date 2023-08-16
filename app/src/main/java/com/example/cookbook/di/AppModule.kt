package com.example.cookbook.di

import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.cookbook.app.CookbookApp
import com.example.cookbook.model.AppState
import com.example.cookbook.model.datasource.RandomRecipeDataSource
import com.example.cookbook.model.datasource.RecipeInformationDataSource
import com.example.cookbook.model.datasource.SearchRecipeDataSource
import com.example.cookbook.model.datasource.retrofit.RetrofitImplementation
import com.example.cookbook.model.interactor.RecipeInfoFragmentInteractor
import com.example.cookbook.model.interactor.HomeFragmentInteractor
import com.example.cookbook.model.interactor.RandomRecipeListInteractor
import com.example.cookbook.model.interactor.SearchFragmentInteractor
import com.example.cookbook.model.repository.local.ILocalRecipesRepository
import com.example.cookbook.model.repository.local.LocalRepositoryImpl
import com.example.cookbook.model.repository.network.NetworkRepository
import com.example.cookbook.model.repository.remoteDataSource.IRepositorySearchRequest
import com.example.cookbook.model.repository.remoteDataSource.SearchRepositoryImpl
import com.example.cookbook.model.room.IRecipesDAO
import com.example.cookbook.model.room.RecipesDatabase
import com.example.cookbook.utils.network.NetworkLiveData
import com.example.cookbook.view.favorite.FavoriteRecipesViewModel
import com.example.cookbook.view.home.randomRecipe.RandomRecipeListViewModel
import com.example.cookbook.view.recipeInfo.RecipeInfoViewModel
import com.example.cookbook.view.home.HomeViewModel
import com.example.cookbook.view.home.healthyRandomRecipe.HealthyRandomRecipeListViewModel
import com.example.cookbook.view.search.SearchViewModel
import com.example.cookbook.view.search.searchResult.SearchResultViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        androidApplication().applicationContext as CookbookApp
    }
    factory { MutableLiveData<AppState>() }
}

val remoteDataSource = module {
    single<IRepositorySearchRequest> {
        SearchRepositoryImpl(
            searchRecipeDataSource = get(),
            randomRecipeDataSource = get(),
            recipeInformationDataSource = get()
        )
    }
    single { RetrofitImplementation() }
    single<SearchRecipeDataSource> { get<RetrofitImplementation>() }
    single<RandomRecipeDataSource> { get<RetrofitImplementation>() }
    single<RecipeInformationDataSource> { get<RetrofitImplementation>() }
}

val localDataBase = module {
    single<ILocalRecipesRepository> { get<LocalRepositoryImpl>() }
    single<IRecipesDAO> { get<RecipesDatabase>().getRecipesDAO() }
    single<RecipesDatabase> {
        Room.databaseBuilder(
            androidApplication(),
            RecipesDatabase::class.java,
            "recipesdatabase.db"
        ).build()
    }
}

val network = module {
    single { NetworkLiveData(androidContext()) }
    single { NetworkRepository(get()) }
}

val homeFragment = module {
    viewModel { HomeViewModel(get()) }
    factory { HomeFragmentInteractor(get(), LocalRepositoryImpl(get<IRecipesDAO>())) }
}

val searchFragment = module {
    single { SearchViewModel(get()) }
    viewModel { SearchResultViewModel(LocalRepositoryImpl(get<IRecipesDAO>())) }
    factory { SearchFragmentInteractor(get()) }
}

val randomRecipeFragment = module {
    viewModel { RandomRecipeListViewModel(get()) }
    viewModel { HealthyRandomRecipeListViewModel(get()) }
    factory {
        RandomRecipeListInteractor(get(), LocalRepositoryImpl(get<IRecipesDAO>()))
    }
}

val recipeInfo = module {
    viewModel { RecipeInfoViewModel(get()) }
    factory { RecipeInfoFragmentInteractor(get()) }
}

val favoritesFragment = module {
    viewModel { FavoriteRecipesViewModel(LocalRepositoryImpl(get<IRecipesDAO>())) }
}

