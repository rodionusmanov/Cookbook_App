package com.example.cookbook.model.data.recipeInformation

data class ExtendedIngredient(
    val aisle: String = "",
    val amount: Double = 0.0,
    val id: Int = 0,
    val image: String = "",
    val measures: Measures = Measures(),
    val meta: List<String> = listOf(),
    val originalName: String = "",
)