package com.example.cookbook.model.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RandomRecipeData(
    val id: Int = 0,
    val image: String = "",
    val readyInMinutes: Int = 0,
    val title: String = ""
) : Parcelable