package com.example.cookbook.model.datasource.DTO.recipeInformation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Equipment(
    val id: Int = 0,
    val image: String = "",
    val name: String = ""
) : Parcelable