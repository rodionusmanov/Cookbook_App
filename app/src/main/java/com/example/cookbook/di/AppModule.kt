package com.example.cookbook.di

import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.cookbook.app.CookbookApp
import com.example.cookbook.model.AppState
import com.example.cookbook.model.datasource.JokeDataSource
import com.example.cookbook.model.datasource.RandomRecipeDataSource
import com.example.cookbook.model.datasource.RecipeInformationDataSource
import com.example.cookbook.model.datasource.SearchRecipeDataSource
import com.example.cookbook.model.datasource.retrofit.RetrofitImplementation
import com.example.cookbook.model.interactor.HomeFragmentInteractor
import com.example.cookbook.model.interactor.RandomRecipeListInteractor
import com.example.cookbook.model.interactor.RecipeFromDatabaseFragmentInteractor
import com.example.cookbook.model.interactor.RecipeInfoFragmentInteractor
import com.example.cookbook.model.interactor.SearchFragmentInteractor
import com.example.cookbook.model.repository.local.ILocalRecipesInfoRepository
import com.example.cookbook.model.repository.local.ILocalRecipesRepository
import com.example.cookbook.model.repository.local.LocalRepositoryImpl
import com.example.cookbook.model.repository.local.LocalRepositoryInfoImpl
import com.example.cookbook.model.repository.network.NetworkRepository
import com.example.cookbook.model.repository.remoteDataSource.IRepositorySearchRequest
import com.example.cookbook.model.repository.remoteDataSource.SearchRepositoryImpl
import com.example.cookbook.model.repository.sharedPreferences.DietaryRestrictionsRepository
import com.example.cookbook.model.room.RecipesDatabase
import com.example.cookbook.model.room.fullRecipe.IRecipesInfoDAO
import com.example.cookbook.utils.network.NetworkLiveData
import com.example.cookbook.view.favorite.FavoriteRecipesViewModel
import com.example.cookbook.view.home.HomeViewModel
import com.example.cookbook.view.home.healthyRandomRecipe.HealthyRandomRecipeListViewModel
import com.example.cookbook.view.home.randomCuisineRecipes.RandomCuisineRecipeListViewModel
import com.example.cookbook.view.home.randomRecipe.RandomRecipeListViewModel
import com.example.cookbook.view.recipeInfo.RecipeInfoViewModel
import com.example.cookbook.view.recipeInfo.adapters.UniversalAdapter
import com.example.cookbook.view.recipeInfoFromDatabase.RecipeInfoFromDatabaseViewModel
import com.example.cookbook.view.search.SearchViewModel
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
            recipeInformationDataSource = get(),
            jokeDataSource = get()
        )
    }
    single { RetrofitImplementation() }
    single<SearchRecipeDataSource> { get<RetrofitImplementation>() }
    single<RandomRecipeDataSource> { get<RetrofitImplementation>() }
    single<RecipeInformationDataSource> { get<RetrofitImplementation>() }
    single<JokeDataSource> { get<RetrofitImplementation>() }
}

val localDataBase = module {
    single<ILocalRecipesRepository> { get<LocalRepositoryImpl>() }
    single<ILocalRecipesInfoRepository> { get<LocalRepositoryInfoImpl>() }
    single<IRecipesInfoDAO> { get<RecipesDatabase>().getRecipesDAO() }
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

val dietaryRestrictionsModule = module {
    single { DietaryRestrictionsRepository(androidContext()) }
}

val homeFragment = module {
    viewModel { HomeViewModel(get()) }
    factory { HomeFragmentInteractor(get(), LocalRepositoryInfoImpl(get<IRecipesInfoDAO>())) }
}

val searchFragment = module {
    viewModel { SearchViewModel(get()) }
    factory { SearchFragmentInteractor(get()) }
}

val randomRecipeFragment = module {
    viewModel { RandomRecipeListViewModel(get()) }
    viewModel { HealthyRandomRecipeListViewModel(get()) }
    viewModel { RandomCuisineRecipeListViewModel(get()) }
    factory {
        RandomRecipeListInteractor(get(), LocalRepositoryInfoImpl(get<IRecipesInfoDAO>()))
    }
}

val recipeInfo = module {
    factory { UniversalAdapter() }
    viewModel { RecipeInfoViewModel(get(), LocalRepositoryInfoImpl(get<IRecipesInfoDAO>())) }
    factory { RecipeInfoFragmentInteractor(get()) }
}

val recipeInfoFromDatabase = module {
    viewModel {
        RecipeInfoFromDatabaseViewModel(
            get(),
            LocalRepositoryInfoImpl(get<IRecipesInfoDAO>())
        )
    }
    factory { RecipeFromDatabaseFragmentInteractor(LocalRepositoryInfoImpl(get<IRecipesInfoDAO>())) }
}

val favoritesFragment = module {
    viewModel { FavoriteRecipesViewModel(LocalRepositoryInfoImpl(get<IRecipesInfoDAO>())) }
}
