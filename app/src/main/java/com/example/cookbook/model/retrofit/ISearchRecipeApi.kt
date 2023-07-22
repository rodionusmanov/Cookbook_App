package com.example.cookbook.model.retrofit

import com.example.cookbook.model.DTO.SearchRecipeListDTO
import com.example.cookbook.utils.HARD_REQUEST
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface ISearchRecipeApi {

    @GET(HARD_REQUEST)
    fun callApi(): Deferred<Response<SearchRecipeListDTO>>
}