package com.example.cookbook.model.datasource.DTO.searchRecipe

import android.os.Parcelable
import com.example.cookbook.model.domain.SearchRecipeData
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchRecipeListDTO(
    @SerializedName("results")
    val searchRecipeList: List<SearchRecipeData>
) : Parcelable