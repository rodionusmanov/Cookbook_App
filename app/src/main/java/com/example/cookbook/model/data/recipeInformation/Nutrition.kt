package com.example.cookbook.model.data.recipeInformation

data class Nutrition(
    val nutrients: List<Nutrient> = listOf(),
    val weightPerServing: WeightPerServing = WeightPerServing()
)