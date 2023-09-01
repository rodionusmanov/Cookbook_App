package com.example.cookbook.model.datasource.retrofit

import com.example.cookbook.model.datasource.DTO.joke.JokeDTO
import com.example.cookbook.model.datasource.DTO.randomRecipe.RandomRecipeListDTO
import com.example.cookbook.model.datasource.DTO.recipeInformation.RecipeInformationDTO
import com.example.cookbook.model.datasource.DTO.searchRecipe.SearchRecipeListDTO
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ISearchRecipeApi {
    @GET("recipes/complexSearch")
    fun searchRecipesAsync(
        @Query("query") query: String?,
        @Query("cuisine") cuisine: String?,
        @Query("excludeIngredients") excludeIngredients: String?,
        @Query("includeIngredients") includeIngredients: String?,
        @Query("diet") diet: String?,
        @Query("intolerances") intolerances: String?,
        @Query("type") dishType: String?,
        @Query("maxReadyTime") maxReadyTime: Int?,
        @Query("minCalories") minCalories: Int?,
        @Query("maxCalories") maxCalories: Int?,
        @Query("offset") offset: Int?
    ): Deferred<Response<SearchRecipeListDTO>>

    @GET("recipes/random")
    fun getRandomRecipesAsync(
        @Query("number") number: Int,
        @Query("tags") tags: String
    ): Deferred<Response<RandomRecipeListDTO>>

    @GET("recipes/{id}/information")
    fun getRecipeFullInformationAsync(
        @Path("id") recipeId: Int,
        @Query("includeNutrition") includeNutrition: Boolean
    ): Deferred<Response<RecipeInformationDTO>>

    @GET("food/jokes/random")
    fun getJokeOfTheDayAsync(
    ): Deferred<Response<JokeDTO>>

}