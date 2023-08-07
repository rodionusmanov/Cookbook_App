package com.example.cookbook.model.datasource.retrofit

import com.example.cookbook.model.datasource.DTO.randomRecipe.RandomRecipeListDTO
import com.example.cookbook.model.datasource.DTO.recipeInformation.RecipeInformationDTO
import com.example.cookbook.model.datasource.DTO.searchRecipe.SearchRecipeListDTO
import com.example.cookbook.utils.SPOONACULAR_API_KEY
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ISearchRecipeApi {
    @GET("recipes/complexSearch")
    fun searchRecipes(
        @Query("query") query: String,
        @Query("includeIngredients") includeIngredients: String,
        @Query("diet") diet: String,
        @Query("intolerances") intolerances: String,
        @Query("apiKey") apiKey: String = SPOONACULAR_API_KEY
    ): Deferred<Response<SearchRecipeListDTO>>

    @GET("recipes/random")
    fun getRandomRecipes(
        @Query("number") number: Int,
        @Query("tags") tags: String,
        @Query("apiKey") apiKey: String = SPOONACULAR_API_KEY
    ): Deferred<Response<RandomRecipeListDTO>>

    @GET("recipes/{id}/information")
    fun getRecipeFullInformation(
        @Path("id") recipeId: Int,
        @Query("includeNutrition") includeNutrition: Boolean,
        @Query("apiKey") apiKey: String = SPOONACULAR_API_KEY
    ): Deferred<Response<RecipeInformationDTO>>
}