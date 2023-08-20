package com.example.cookbook.model.datasource.retrofit

import android.util.Log
import com.example.cookbook.model.datasource.DTO.joke.JokeDTO
import com.example.cookbook.model.datasource.DTO.randomRecipe.RandomRecipeListDTO
import com.example.cookbook.model.datasource.DTO.recipeInformation.RecipeInformationDTO
import com.example.cookbook.model.datasource.DTO.searchRecipe.SearchRecipeListDTO
import com.example.cookbook.model.datasource.JokeDataSource
import com.example.cookbook.model.datasource.RandomRecipeDataSource
import com.example.cookbook.model.datasource.RecipeInformationDataSource
import com.example.cookbook.model.datasource.SearchRecipeDataSource
import com.example.cookbook.utils.COMPLEX_SEARCH_RECIPE_API
import com.example.cookbook.utils.DEFAULT_RECIPE_NUMBER
import com.example.cookbook.utils.SPOONACULAR_API_KEY
import com.example.cookbook.utils.SPOONACULAR_HEALTHY_DIET_TAG
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitImplementation : SearchRecipeDataSource, RandomRecipeDataSource,
    RecipeInformationDataSource, JokeDataSource {

    private val baseInterceptor = BaseInterceptor.interceptor
    override suspend fun getSearchResult(
        request: String,
        ingredients: String,
        userDiets: String,
        userIntolerances: String
    ): Response<SearchRecipeListDTO> {
        Log.d("@@@", "User Diets: $userDiets")
        Log.d("@@@", "User Intolerances: $userIntolerances")

        return getService(baseInterceptor).searchRecipesAsync(
            request, ingredients, userDiets, userIntolerances, ""
        ).await()
    }

    override suspend fun getRandomRecipes(
        userDiets: String,
        userIntolerances: String
    ): Response<RandomRecipeListDTO> {
        val tags = arrayOf(userDiets, userIntolerances)
            .joinToString(",")
        return getService(baseInterceptor).getRandomRecipesAsync(
            DEFAULT_RECIPE_NUMBER, tags
        ).await()
    }

    override suspend fun getHealthyRandomRecipes(): Response<RandomRecipeListDTO> {
        return getService(baseInterceptor).getRandomRecipesAsync(
            DEFAULT_RECIPE_NUMBER, SPOONACULAR_HEALTHY_DIET_TAG
        ).await()
    }

    override suspend fun getRandomCuisineRecipes(
        cuisine: String,
        userIntolerances: String
    ): Response<RandomRecipeListDTO> {
        val tags = arrayOf(userIntolerances, cuisine)
            .joinToString(",")
        return getService(baseInterceptor).getRandomRecipesAsync(
            DEFAULT_RECIPE_NUMBER, tags
        ).await()
    }

    override suspend fun getRecipesByType(
        dishType: String,
        userDiets: String,
        userIntolerances: String
    ): Response<SearchRecipeListDTO> {
        return getService(baseInterceptor).searchRecipesAsync(
            "", "", userDiets, userIntolerances, dishType
        ).await()
    }

    override suspend fun getRecipeFullInformation(id: Int): Response<RecipeInformationDTO> {
        return getService(baseInterceptor).getRecipeFullInformationAsync(
            id, true
        ).await()
    }

    private fun getService(interceptor: Interceptor): ISearchRecipeApi {
        return createRetrofit(interceptor).create(ISearchRecipeApi::class.java)
    }

    private fun createRetrofit(interceptor: Interceptor): Retrofit {
        return Retrofit.Builder().baseUrl(COMPLEX_SEARCH_RECIPE_API).addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        ).addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(createOkHttpClient(interceptor)).build()
    }

    private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()

        httpClient.addInterceptor { chain ->
            var request = chain.request()
            val url =
                request.url.newBuilder().addQueryParameter("apiKey", SPOONACULAR_API_KEY).build()

            request = request.newBuilder().url(url).build()
            chain.proceed(request)
        }

        httpClient.addInterceptor(interceptor)

        return httpClient.build()
    }

    override suspend fun getJokeText(): Response<JokeDTO> {
        return getService(baseInterceptor).getJokeOfTheDayAsync().await()
    }
}