package com.example.cookbook.model.datasource.DTO.recipeInformation

import android.os.Parcelable
import com.example.cookbook.model.domain.UniversalItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class Step(
    val equipment: List<Equipment> = listOf(),
    val ingredients: List<Ingredient> = listOf(),
    val length: Length? = null,
    val number: Int = 0,
    val step: String = ""
) : Parcelable, UniversalItem