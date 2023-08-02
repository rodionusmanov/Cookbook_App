package com.example.cookbook.model.datasource.DTO.recipeInformation

data class Step(
    val equipment: List<Equipment> = listOf(),
    val ingredients: List<Ingredient> = listOf(),
    val length: Length? = null,
    val number: Int = 0,
    val step: String = ""
)