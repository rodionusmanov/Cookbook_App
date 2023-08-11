package com.example.cookbook.model.datasource.DTO.recipeInformation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Length(
    val number: Int = 0,
    val unit: String = ""
) : Parcelable