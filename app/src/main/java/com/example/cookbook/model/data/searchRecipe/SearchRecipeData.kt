package com.example.cookbook.model.data.searchRecipe

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchRecipeData(
    val id: Int = 0,
    val title: String = "",
    val image: String = ""
): Parcelable
