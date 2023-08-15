package com.example.cookbook.model.room.fullRecipe

import androidx.room.TypeConverters
import com.example.cookbook.model.room.DataTypeConverters

data class ProteinNutrient(
    @TypeConverters(DataTypeConverters::class)
    val proteinAmount: Double = 0.0,
    val proteinName: String = "",
    @TypeConverters(DataTypeConverters::class)
    val proteinPercentOfDailyNeeds: Double = 0.0,
    val proteinUnit: String = ""
)
