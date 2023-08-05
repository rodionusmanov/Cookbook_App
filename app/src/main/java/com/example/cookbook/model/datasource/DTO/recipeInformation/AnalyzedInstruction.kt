package com.example.cookbook.model.datasource.DTO.recipeInformation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AnalyzedInstruction(
    val steps: List<Step> = listOf()
) : Parcelable