package com.example.cookbook.model.datasource.DTO.recipeInformation

data class Nutrition(
    val nutrients: List<Nutrient> = listOf(),
    val weightPerServing: WeightPerServing = WeightPerServing()
)