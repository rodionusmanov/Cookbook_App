package com.example.cookbook.model.datasource.DTO.recipeInformation

import android.os.Parcelable
import com.example.cookbook.model.domain.UniversalItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExtendedIngredient(
    val aisle: String = "",
    val id: Int = 0,
    val image: String = "",
    val measures: Measures = Measures(),
    val meta: List<String> = listOf(),
    val originalName: String = "",
) : Parcelable, UniversalItem