package com.example.cookbook.model.repository.remoteDataSource

import android.util.Log
import com.example.cookbook.model.datasource.JokeDataSource
import com.example.cookbook.model.datasource.RandomRecipeDataSource
import com.example.cookbook.model.datasource.RecipeInformationDataSource
import com.example.cookbook.model.datasource.SearchRecipeDataSource
import com.example.cookbook.model.datasource.retrofit.BaseInterceptor
import com.example.cookbook.model.domain.RandomRecipeData
import com.example.cookbook.model.domain.RecipeInformation
import com.example.cookbook.model.domain.SearchRecipeData
import com.example.cookbook.utils.MappingUtils
import retrofit2.Response

class SearchRepositoryImpl(
    private val searchRecipeDataSource: SearchRecipeDataSource,
    private val randomRecipeDataSource: RandomRecipeDataSource,
    private val recipeInformationDataSource: RecipeInformationDataSource,
    private val jokeDataSource: JokeDataSource
) : IRepositorySearchRequest {

    private val mapper = MappingUtils()

    override suspend fun getSearchResult(
        request: String,
        ingredients: String
    ): List<SearchRecipeData> {

        val response = searchRecipeDataSource.getSearchResult(request, ingredients)
        return parseResponse(response) { responseDto ->
            responseDto.searchRecipeList.map { recipe ->
                mapper.mapRecipeData(recipe) as SearchRecipeData
            }
        }
    }

    override suspend fun getRandomRecipes(): List<RandomRecipeData> {
        val response = randomRecipeDataSource.getRandomRecipes()
        return parseResponse(response) { responseDto ->
            responseDto.recipes.map { recipe ->
                mapper.mapRecipeData(recipe) as RandomRecipeData
            }
        }
    }

    override suspend fun getRandomCuisineRecipes(cuisine: String): List<RandomRecipeData> {
        val response = randomRecipeDataSource.getRandomRecipes()
        return parseResponse(response) { responseDto ->
            responseDto.recipes.map { recipe ->
                mapper.mapRecipeData(recipe) as RandomRecipeData
            }
        }
    }

    override suspend fun getRecipesByType(dishType: String): List<SearchRecipeData> {
        Log.d("@@@", "SearchRepository working get query: $dishType")
        val response = searchRecipeDataSource.getRecipesByType(dishType)
        return parseResponse(response) { responseDTO ->
            responseDTO.searchRecipeList.map { recipe ->
                mapper.mapRecipeData(recipe) as SearchRecipeData
            }
        }
    }

    override suspend fun getHealthyRandomRecipes(): List<RandomRecipeData> {
        val response = randomRecipeDataSource.getHealthyRandomRecipes()
        return parseResponse(response) { responseDto ->
            responseDto.recipes.map { recipe ->
                mapper.mapRecipeData(recipe) as RandomRecipeData
            }
        }
    }

    override suspend fun getJokeText(): String {
        val response = jokeDataSource.getJokeText()
        return parseResponse(response) { jokeDTO ->
            jokeDTO.text
        }
    }

    override suspend fun getRecipeInfo(id: Int): RecipeInformation {
        val response = recipeInformationDataSource.getRecipeFullInformation(id)
        return parseResponse(response) { dto ->
            val nutritionMap = dto.nutrition.nutrients.associateBy { it.name }
            mapper.mapRecipeInformation(dto, nutritionMap)
        }
    }

    private fun <T, R> parseResponse(response: Response<T>, dataSelector: (T) -> R): R {

        Log.d("@@@", "ParseResponse Response status code: ${response.code()}")
        Log.d("@@@", "ParseResponse Response body: ${response.body()?.toString()}")

        val responseStatusCode = BaseInterceptor.interceptor.getResponseCode()

        if (response.isSuccessful && response.body() != null) {
            return when (responseStatusCode) {
                BaseInterceptor.ServerResponseStatusCode.SUCCESS -> {
                    val body = response.body()
                    body?.let { dataSelector(it) } ?: throw Exception("Body should not be null")
                }

                BaseInterceptor.ServerResponseStatusCode.REDIRECTION ->
                    throw Exception("Redirection occurred")

                BaseInterceptor.ServerResponseStatusCode.CLIENT_ERROR -> {
                    val errorMessage = response.errorBody()?.string() ?: "Client Error"
                    throw Exception("Client Error: $errorMessage")
                }

                BaseInterceptor.ServerResponseStatusCode.SERVER_ERROR -> {
                    val errorMessage = response.errorBody()?.string() ?: "Server Error"
                    throw Exception("A server error: $errorMessage occured. Please try again later.")
                }

                else -> throw Exception("Unknown Error")
            }
        } else {
            throw Exception("Response was not successful: ${response.message()}")
        }
    }
}
