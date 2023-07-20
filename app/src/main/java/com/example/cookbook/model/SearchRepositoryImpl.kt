package com.example.cookbook.model

import android.util.Log
import com.example.cookbook.model.DTO.SearchRecipeListDTO
import com.example.cookbook.model.retrofit.ISearchRecipeApi
import com.example.cookbook.utils.convertSearchDTOToRecipeList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class SearchRepositoryImpl(
    private val api: ISearchRecipeApi
) : IRepositorySearchRequestToRecipeList {

    override fun getSearchResult(request: String, callback: ISearchRecipeCallback) {
        api.callApi().enqueue(object : Callback<SearchRecipeListDTO> {
            override fun onResponse(
                call: Call<SearchRecipeListDTO>,
                response: Response<SearchRecipeListDTO>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onResponse(convertSearchDTOToRecipeList(response.body()!!))
                } else {
                    callback.onFailure(IOException("4xx"))
                    Log.e("4xx", response.body().toString())
                }
            }

            override fun onFailure(call: Call<SearchRecipeListDTO>, t: Throwable) {
                callback.onFailure(IOException(t))
                Log.e("4xx", t.toString())
            }

        })
    }
}