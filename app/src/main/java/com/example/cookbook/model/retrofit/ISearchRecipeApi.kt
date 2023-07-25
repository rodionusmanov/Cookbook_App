package com.example.cookbook.model.retrofit

import com.example.cookbook.model.DTO.SearchRecipeListDTO
import com.example.cookbook.utils.SPOONACULAR_API_KEY
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ISearchRecipeApi {

    @GET("complexSearch")
    fun callApi(
        @Query("query") query: String,
        @Query("includeIngredients") includeIngredients: String,
        @Query("apiKey") apiKey: String = SPOONACULAR_API_KEY
    ): Deferred<Response<SearchRecipeListDTO>>
}