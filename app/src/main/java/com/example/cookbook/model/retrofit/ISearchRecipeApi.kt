package com.example.cookbook.model.retrofit

import com.example.cookbook.model.DTO.SearchRecipeListDTO
import com.example.cookbook.utils.HARD_REQUEST
import com.example.cookbook.utils.SPOONACULAR_API_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ISearchRecipeApi {

    @GET(HARD_REQUEST)
    fun callApi(): Call<SearchRecipeListDTO>
}