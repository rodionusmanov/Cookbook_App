package com.example.cookbook.model.datasource.DTO.recipeInformation

data class ExtendedIngredient(
    val aisle: String = "",
    val id: Int = 0,
    val image: String = "",
    val measures: Measures = Measures(),
    val meta: List<String> = listOf(),
    val originalName: String = "",
)