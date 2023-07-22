package com.example.cookbook.model.datasource

import com.example.cookbook.model.DTO.SearchRecipeListDTO
import com.example.cookbook.model.retrofit.ISearchRecipeApi
import com.example.cookbook.utils.COMPLEX_SEARCH_RECIPE_API
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitImplementation : DataSource<Response<SearchRecipeListDTO>>{
    override suspend fun getData(request: String): Response<SearchRecipeListDTO> {
        return getService().callApi().await()
    }

    private fun getService() : ISearchRecipeApi{
        return createRetrofit().create(ISearchRecipeApi::class.java)
    }

    private fun createRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(COMPLEX_SEARCH_RECIPE_API)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }
}