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
import com.example.cookbook.model.repository.local.ILocalRecipesRepository
import com.example.cookbook.model.repository.local.LocalRepositoryImpl
import com.example.cookbook.model.repository.network.NetworkRepository
import com.example.cookbook.model.repository.remoteDataSource.IRepositorySearchRequest
import com.example.cookbook.model.repository.remoteDataSource.SearchRepositoryImpl
import com.example.cookbook.model.room.IRecipesDAO
import com.example.cookbook.model.room.RecipesDatabase
import com.example.cookbook.utils.network.NetworkLiveData
import com.example.cookbook.viewModel.favorite.FavoriteRecipesViewModel
import com.example.cookbook.viewModel.home.randomRecipeList.RandomRecipeListViewModel
import com.example.cookbook.viewModel.recipeInfo.RecipeInfoViewModel
import com.example.cookbook.viewModel.home.HomeViewModel
import com.example.cookbook.viewModel.searchRecipe.SearchResultViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        androidApplication().applicationContext as CookbookApp
    }

    single<IRepositorySearchRequest> {
        SearchRepositoryImpl(
            searchRecipeDataSource = get(),
            randomRecipeDataSource = get(),
            recipeInformationDataSource = get()
        )
    }
    single<RetrofitImplementation> { RetrofitImplementation() }

    single<SearchRecipeDataSource> { get<RetrofitImplementation>() }
    single<RandomRecipeDataSource> { get<RetrofitImplementation>() }
    single<RecipeInformationDataSource> { get<RetrofitImplementation>() }

    factory {
        MutableLiveData<AppState>()
    }

    factory { HomeFragmentInteractor(get(), LocalRepositoryImpl(get<IRecipesDAO>())) }
    factory { RecipeInfoFragmentInteractor(get()) }

    viewModel { HomeViewModel(get()) }

    viewModel { FavoriteRecipesViewModel(LocalRepositoryImpl(get<IRecipesDAO>())) }

    viewModel { SearchResultViewModel(LocalRepositoryImpl(get<IRecipesDAO>())) }

    viewModel { RecipeInfoViewModel(get()) }

    viewModel { RandomRecipeListViewModel(LocalRepositoryImpl(get<IRecipesDAO>())) }

    single { NetworkLiveData(androidContext()) }
    single { NetworkRepository(get()) }

    single<ILocalRecipesRepository> {
        get<LocalRepositoryImpl>()
    }

    single<RecipesDatabase> {
        Room.databaseBuilder(
            androidApplication(),
            RecipesDatabase::class.java,
            "recipesdatabase.db"
        ).build()
    }
    single<IRecipesDAO> { get<RecipesDatabase>().getRecipesDAO() }
}