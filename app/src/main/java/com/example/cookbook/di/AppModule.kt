package com.example.cookbook.di

import androidx.lifecycle.MutableLiveData
import com.example.cookbook.CookbookApp
import com.example.cookbook.model.AppState
import com.example.cookbook.model.datasource.RetrofitImplementation
import com.example.cookbook.model.interactor.SearchFragmentInteractor
import com.example.cookbook.model.repository.IRepositorySearchRequestToRecipeList
import com.example.cookbook.model.repository.SearchRepositoryImpl
import com.example.cookbook.viewModel.searchRecipe.SearchRecipeViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        androidApplication().applicationContext as CookbookApp
    }

    single<IRepositorySearchRequestToRecipeList> {
        SearchRepositoryImpl(RetrofitImplementation())
    }

    factory {
        MutableLiveData<AppState>()
    }

    factory { SearchFragmentInteractor( get() ) }

    viewModel {
        SearchRecipeViewModel( get() )
    }
}