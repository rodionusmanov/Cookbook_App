package com.example.cookbook.model.datasource.retrofit

import com.example.cookbook.model.DTO.SearchRecipeListDTO
import com.example.cookbook.model.data.randomRecipe.RandomRecipeDTO
import com.example.cookbook.model.datasource.DataSource
import com.example.cookbook.utils.COMPLEX_SEARCH_RECIPE_API
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitImplementation : DataSource<Response<SearchRecipeListDTO>> {
    override suspend fun getData(
        request: String,
        ingredients: String
    ): Response<SearchRecipeListDTO> {
        return getService().searchRecipes(
            request, ingredients, DEFAULT_USER_DIET, DEFAULT_USER_INTOLERANCE
        ).await()
    }
    suspend fun getRandomRecipes() : Response<RandomRecipeDTO> {
        val userDietAndIntolerance = arrayOf(DEFAULT_USER_DIET, DEFAULT_USER_INTOLERANCE)
            .joinToString(",")
        return getService().getRandomRecipes(
            DEFAULT_RECIPE_NUMBER, userDietAndIntolerance).await()
    }

    suspend fun getRecipeFullInformation(id: Int) : Response<SearchRecipeListDTO> {
        return getService().getRecipeFullInformation(id, false).await()
    }

    private fun getService() : ISearchRecipeApi {
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

    companion object {
        const val DEFAULT_RECIPE_NUMBER = 10
        const val DEFAULT_USER_DIET = "vegetarian"
        const val DEFAULT_USER_INTOLERANCE = "peanut"
    }
}