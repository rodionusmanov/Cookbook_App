package com.example.cookbook.model.room.fullRecipe

import androidx.room.TypeConverters
import com.example.cookbook.model.room.DataTypeConverters

data class CaloriesNutrient(
    @TypeConverters(DataTypeConverters::class)
    val caloriesAmount: Double = 0.0,
    val caloriesName: String = "",
    @TypeConverters(DataTypeConverters::class)
    val caloriesPercentOfDailyNeeds: Double = 0.0,
    val caloriesUnit: String = ""
)
