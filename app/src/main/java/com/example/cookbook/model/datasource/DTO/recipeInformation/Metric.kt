package com.example.cookbook.model.datasource.DTO.recipeInformation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Metric(
    val amount: Double = 0.0,
    val unitLong: String = "",
    val unitShort: String = ""
) : Parcelable