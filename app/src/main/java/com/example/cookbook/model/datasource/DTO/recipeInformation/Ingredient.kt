package com.example.cookbook.model.datasource.DTO.recipeInformation

import android.os.Parcelable
import com.example.cookbook.model.domain.UniversalItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ingredient(
    val id: Int = 0,
    val image: String = "",
    val name: String = ""
) : Parcelable, UniversalItem