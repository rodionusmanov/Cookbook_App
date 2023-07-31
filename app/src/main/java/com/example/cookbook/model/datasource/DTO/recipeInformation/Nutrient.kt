package com.example.cookbook.model.datasource.DTO.recipeInformation

data class Nutrient(
    val amount: Double = 0.0,
    val name: String = "",
    val percentOfDailyNeeds: Double = 0.0,
    val unit: String = ""
)