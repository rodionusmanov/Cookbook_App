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
import com.example.cookbook.model.interactor.FavoriteFragmentInteractor
import com.example.cookbook.model.interactor.HomeFragmentInteractor
import com.example.cookbook.model.interactor.RandomRecipeListInteractor
import com.example.cookbook.model.interactor.RecipeFromDatabaseFragmentInteractor
import com.example.cookbook.model.interactor.RecipeInfoFragmentInteractor
import com.example.cookbook.model.interactor.SearchFragmentInteractor
import com.example.cookbook.model.repository.local.ILocalRecipesInfoRepository
import com.example.cookbook.model.repository.local.LocalRepositoryInfoImpl
import com.example.cookbook.model.repository.network.NetworkRepository
import com.example.cookbook.model.repository.remote.IRepositorySearchRequest
import com.example.cookbook.model.repository.remote.SearchRepositoryImpl
import com.example.cookbook.model.repository.sharedPreferences.SharedPreferencesRepository
import com.example.cookbook.model.room.RecipesDatabase
import com.example.cookbook.utils.network.NetworkLiveData
import com.example.cookbook.view.favorite.FavoriteRecipesViewModel
import com.example.cookbook.view.home.HomeViewModel
import com.example.cookbook.view.home.healthyRandomRecipe.HealthyRandomRecipeListViewModel
import com.example.cookbook.view.home.randomCuisineRecipes.RandomCuisineRecipeListViewModel
import com.example.cookbook.view.home.randomRecipe.RandomRecipeListViewModel
import com.example.cookbook.view.myProfile.MyProfileViewModel
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
    single<ILocalRecipesInfoRepository> { get<LocalRepositoryInfoImpl>() }
    single { get<RecipesDatabase>().getRecipesDAO() }
    single {
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
    factory { HomeFragmentInteractor(get()) }
}

val searchFragment = module {
    viewModel { SearchViewModel(get()) }
    factory { SearchFragmentInteractor(get(), get()) }
}

val randomRecipeFragment = module {
    viewModel { RandomRecipeListViewModel(get()) }
    viewModel { HealthyRandomRecipeListViewModel(get()) }
    viewModel { RandomCuisineRecipeListViewModel(get()) }
    factory {
        RandomRecipeListInteractor(get(), LocalRepositoryInfoImpl(get()), get())
    }
}

val recipeInfo = module {
    factory { UniversalAdapter() }
    viewModel { RecipeInfoViewModel(get(), LocalRepositoryInfoImpl(get())) }
    factory { RecipeInfoFragmentInteractor(get()) }
}

val recipeInfoFromDatabase = module {
    viewModel {
        RecipeInfoFromDatabaseViewModel(
            get(),
            LocalRepositoryInfoImpl(get())
        )
    }
    factory { RecipeFromDatabaseFragmentInteractor(LocalRepositoryInfoImpl(get())) }
}

val favoritesFragment = module {
    viewModel {
        FavoriteRecipesViewModel(
            get()
        )
    }
    factory { FavoriteFragmentInteractor(LocalRepositoryInfoImpl(get())) }
}

val myProfileFragment = module {
    viewModel { MyProfileViewModel(get()) }
    single { SharedPreferencesRepository(androidContext()) }
}
