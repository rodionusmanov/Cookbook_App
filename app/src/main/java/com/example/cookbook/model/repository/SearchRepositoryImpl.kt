package com.example.cookbook.model.repository

import android.util.Log
import com.example.cookbook.domain.Recipe
import com.example.cookbook.model.DTO.SearchRecipeListDTO
import com.example.cookbook.model.datasource.DataSource
import com.example.cookbook.utils.convertSearchDTOToRecipeList
import retrofit2.Response
import java.io.IOException

class SearchRepositoryImpl(
    private val dataSource: DataSource<Response<SearchRecipeListDTO>>
) : IRepositorySearchRequestToRecipeList {

    override suspend fun getSearchResult(request: String, ingredients: String): List<Recipe> {

        val response = dataSource.getData(request, ingredients)

        if (response.isSuccessful && response.body() != null) {
            return convertSearchDTOToRecipeList(response.body()!!)
        } else {
            Log.e("4xx", response.body().toString())
            throw IOException("4xx")
        }
    }
}