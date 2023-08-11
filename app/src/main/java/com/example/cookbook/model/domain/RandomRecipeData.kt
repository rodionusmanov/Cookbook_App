package com.example.cookbook.model.domain

import kotlinx.parcelize.Parcelize

@Parcelize
data class RandomRecipeData(
    override val id: Int = 0,
    override val image: String = "",
    override val readyInMinutes: Int = 0,
    override val title: String = ""
) : BaseRecipeData