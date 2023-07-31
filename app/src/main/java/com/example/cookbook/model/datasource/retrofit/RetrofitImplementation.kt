package com.example.cookbook.model.datasource.retrofit

import com.example.cookbook.model.datasource.DTO.searchRecipe.SearchRecipeListDTO
import com.example.cookbook.model.datasource.DTO.randomRecipe.RandomRecipeListDTO
import com.example.cookbook.model.datasource.DTO.recipeInformation.RecipeInformationDTO
import com.example.cookbook.model.datasource.RandomRecipeDataSource
import com.example.cookbook.model.datasource.RecipeInformationDataSource
import com.example.cookbook.model.datasource.SearchRecipeDataSource
import com.example.cookbook.utils.COMPLEX_SEARCH_RECIPE_API
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitImplementation : SearchRecipeDataSource, RandomRecipeDataSource,
    RecipeInformationDataSource {
    override suspend fun getSearchResult(
        request: String,
        ingredients: String
    ): Response<SearchRecipeListDTO> {
        return getService().searchRecipes(
            request, ingredients, DEFAULT_USER_DIET, DEFAULT_USER_INTOLERANCE
        ).await()
    }

    override suspend fun getRandomRecipes(): Response<RandomRecipeListDTO> {
        val userDietAndIntolerance = arrayOf(DEFAULT_USER_DIET, DEFAULT_USER_INTOLERANCE)
            .joinToString(",")
        return getService().getRandomRecipes(
            DEFAULT_RECIPE_NUMBER, userDietAndIntolerance
        ).await()
    }

    override suspend fun getRecipeFullInformation(id: Int): Response<RecipeInformationDTO> {
        return getService().getRecipeFullInformation(id, true).await()
    }

    private fun getService(): ISearchRecipeApi {
        return createRetrofit().create(ISearchRecipeApi::class.java)
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(COMPLEX_SEARCH_RECIPE_API)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            )
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    companion object {
        const val DEFAULT_RECIPE_NUMBER = 10
        const val DEFAULT_USER_DIET = "vegetarian"
        const val DEFAULT_USER_INTOLERANCE = "peanut"
    }
}