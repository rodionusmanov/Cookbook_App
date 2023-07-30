package com.example.cookbook.model.DTO

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchRecipeListDTO(
    @SerializedName("results")
    val searchRecipeList: List<SearchRecipeData>
) : Parcelable