package com.example.cookbook.model.domain

import android.os.Parcelable

interface BaseRecipeData : Parcelable {
    val id: Int
    val title: String
    val image: String
}