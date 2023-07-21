package com.example.cookbook.di

import androidx.lifecycle.MutableLiveData
import com.example.cookbook.CookbookApp
import com.example.cookbook.model.AppState
import com.example.cookbook.model.IRepositorySearchRequestToRecipeList
import com.example.cookbook.model.SearchRepositoryImpl
import com.example.cookbook.model.retrofit.ISearchRecipeApi
import com.example.cookbook.utils.COMPLEX_SEARCH_RECIPE_API
import com.example.cookbook.viewModel.searchRecipe.SearchRecipeViewModel
import com.google.gson.GsonBuilder
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single {
        androidApplication().applicationContext as CookbookApp
    }

    single {
        Retrofit.Builder()
            .baseUrl(COMPLEX_SEARCH_RECIPE_API)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            )
            .build().create(ISearchRecipeApi::class.java)
    }

    single<IRepositorySearchRequestToRecipeList> {
        SearchRepositoryImpl(get())
    }

    factory {
        MutableLiveData<AppState>()
    }

    viewModel {
        SearchRecipeViewModel(get(), get())
    }
}