package com.example.cookbook.model.repository.remoteDataSource

import com.example.cookbook.model.datasource.RandomRecipeDataSource
import com.example.cookbook.model.datasource.RecipeInformationDataSource
import com.example.cookbook.model.datasource.SearchRecipeDataSource
import com.example.cookbook.model.domain.RandomRecipeData
import com.example.cookbook.model.domain.RecipeInformation
import com.example.cookbook.model.domain.SearchRecipeData
import com.example.cookbook.utils.MappingUtils
import retrofit2.Response

class SearchRepositoryImpl(
    private val searchRecipeDataSource: SearchRecipeDataSource,
    private val randomRecipeDataSource: RandomRecipeDataSource,
    private val recipeInformationDataSource: RecipeInformationDataSource
) : IRepositorySearchRequest {

    private val mapper = MappingUtils()

    override suspend fun getSearchResult(
        request: String,
        ingredients: String
    ): List<SearchRecipeData> {

        val response = searchRecipeDataSource.getSearchResult(request, ingredients)
        return parseResponse(response) { it.searchRecipeList }
    }

    override suspend fun getRandomRecipes(): List<RandomRecipeData> {

        val response = randomRecipeDataSource.getRandomRecipes()
        return parseResponse(response) { it.randomRecipeData }
    }

    override suspend fun getRecipeInfo(id: Int): RecipeInformation {
        val response = recipeInformationDataSource.getRecipeFullInformation(id)
        return parseResponse(response) { dto ->
            val nutritionMap = dto.nutrition.nutrients.associateBy { it.name }
            mapper.mapRecipeInformation(dto, nutritionMap)
        }
    }

    private fun <T, R> parseResponse(response: Response<T>, dataSelector: (T) -> R): R {
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return dataSelector(body)
            } else {
                throw Exception("Response body is null")
            }
        } else {
            throw Exception("Response was not successful: ${response.message()}")
        }
    }
}