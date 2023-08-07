package com.example.cookbook.model.domain

import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchRecipeData(
    override val id: Int = 0,
    override val title: String = "",
    override val image: String = "",
): BaseRecipeData
