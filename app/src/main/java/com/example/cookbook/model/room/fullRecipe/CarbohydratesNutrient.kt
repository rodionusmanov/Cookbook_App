package com.example.cookbook.model.room.fullRecipe

import androidx.room.TypeConverters
import com.example.cookbook.model.room.DataTypeConverters

data class CarbohydratesNutrient(
    @TypeConverters(DataTypeConverters::class)
    val carbohydratesAmount: Double = 0.0,
    val carbohydratesName: String = "",
    @TypeConverters(DataTypeConverters::class)
    val carbohydratesPercentOfDailyNeeds: Double = 0.0,
    val carbohydratesUnit: String = ""
)
