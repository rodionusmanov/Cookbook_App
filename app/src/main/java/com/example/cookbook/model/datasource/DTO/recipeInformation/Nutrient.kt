package com.example.cookbook.model.datasource.DTO.recipeInformation

import androidx.room.TypeConverters
import com.example.cookbook.model.room.DataTypeConverters

data class Nutrient(
    @TypeConverters(DataTypeConverters::class)
    val amount: Double = 0.0,
    val name: String = "",
    @TypeConverters(DataTypeConverters::class)
    val percentOfDailyNeeds: Double = 0.0,
    val unit: String = ""
)