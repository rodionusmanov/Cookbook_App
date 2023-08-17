package com.example.cookbook.model.room.fullRecipe

import androidx.room.TypeConverters
import com.example.cookbook.model.room.DataTypeConverters

data class FatNutrient(
    @TypeConverters(DataTypeConverters::class)
    val fatAmount: Double = 0.0,
    val fatName: String = "",
    @TypeConverters(DataTypeConverters::class)
    val fatPercentOfDailyNeeds: Double = 0.0,
    val fatUnit: String = ""
)
