package com.example.cookbook.model.domain

import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchRecipeData(
    val id: Int = 0,
    val title: String = "",
    val image: String = "",
): BaseRecipeData
