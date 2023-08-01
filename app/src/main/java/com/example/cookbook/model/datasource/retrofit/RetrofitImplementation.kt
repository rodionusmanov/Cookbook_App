package com.example.cookbook.model.datasource.retrofit

import com.example.cookbook.model.datasource.DTO.searchRecipe.SearchRecipeListDTO
import com.example.cookbook.model.datasource.DTO.randomRecipe.RandomRecipeListDTO
import com.example.cookbook.model.datasource.DTO.recipeInformation.RecipeInformationDTO
import com.example.cookbook.model.datasource.RandomRecipeDataSource
import com.example.cookbook.model.datasource.RecipeInformationDataSource
import com.example.cookbook.model.datasource.SearchRecipeDataSource
import com.example.cookbook.utils.COMPLEX_SEARCH_RECIPE_API
import com.example.cookbook.utils.SPOONACULAR_API_KEY
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitImplementation : SearchRecipeDataSource, RandomRecipeDataSource,
    RecipeInformationDataSource {

    private val baseInterceptor = BaseInterceptor.interceptor
    override suspend fun getSearchResult(
        request: String,
        ingredients: String
    ): Response<SearchRecipeListDTO> {
        return getService(baseInterceptor).searchRecipes(
            request, ingredients, DEFAULT_USER_DIET, DEFAULT_USER_INTOLERANCE
        ).await()
    }

    override suspend fun getRandomRecipes(): Response<RandomRecipeListDTO> {
        val userDietAndIntolerance = arrayOf(DEFAULT_USER_DIET, DEFAULT_USER_INTOLERANCE)
            .joinToString(",")
        return getService(baseInterceptor).getRandomRecipes(
            DEFAULT_RECIPE_NUMBER, userDietAndIntolerance
        ).await()
    }

    override suspend fun getRecipeFullInformation(id: Int): Response<RecipeInformationDTO> {
        return getService(baseInterceptor).getRecipeFullInformation(id, true).await()
    }

    private fun getService(interceptor: Interceptor): ISearchRecipeApi {
        return createRetrofit(interceptor).create(ISearchRecipeApi::class.java)
    }

    private fun createRetrofit(interceptor: Interceptor): Retrofit {
        return Retrofit.Builder()
            .baseUrl(COMPLEX_SEARCH_RECIPE_API)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            )
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(createOkHttpClient(interceptor))
            .build()
    }

    private fun createOkHttpClient(interceptor: Interceptor) : OkHttpClient {
        val httpClient = OkHttpClient.Builder()

        httpClient.addInterceptor { chain ->
            var request = chain.request()
            val url = request.url.newBuilder()
                .addQueryParameter("apiKey", SPOONACULAR_API_KEY)
                .build()

            request = request.newBuilder().url(url).build()
            chain.proceed(request)
        }

        httpClient.addInterceptor(interceptor)

        httpClient.addInterceptor(HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY))

        return httpClient.build()
    }

    companion object {
        const val DEFAULT_RECIPE_NUMBER = 10
        const val DEFAULT_USER_DIET = "vegetarian"
        const val DEFAULT_USER_INTOLERANCE = "peanut"
    }
}