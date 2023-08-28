package com.example.cookbook.model.repository.remote

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
        ingredients: String,
        userDiets: String,
        userIntolerances: String,
        dishType: String
    ): List<SearchRecipeData> {

        val response = searchRecipeDataSource
            .getSearchResult(request, ingredients, userDiets, userIntolerances, dishType)
        return parseResponse(response) { responseDto ->
            responseDto.searchRecipeList.map { recipe ->
                mapper.mapRecipeData(recipe) as SearchRecipeData
            }
        }
    }

    override suspend fun getRandomRecipes(
        userDiets: String,
        userIntolerances: String
    )
            : List<RandomRecipeData> {
        val response = randomRecipeDataSource.getRandomRecipes(userDiets, userIntolerances)
        return parseResponse(response) { responseDto ->
            responseDto.recipes.map { recipe ->
                mapper.mapRecipeData(recipe) as RandomRecipeData
            }
        }
    }

    override suspend fun getRandomCuisineRecipes(
        cuisine: String,
        userIntolerances: String
    ): List<RandomRecipeData> {
        val response = randomRecipeDataSource.getRandomCuisineRecipes(cuisine, userIntolerances)
        return parseResponse(response) { responseDto ->
            responseDto.recipes.map { recipe ->
                mapper.mapRecipeData(recipe) as RandomRecipeData
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
