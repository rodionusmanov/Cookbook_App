package com.example.cookbook.model.room

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.cookbook.model.datasource.DTO.recipeInformation.WeightPerServing
import com.example.cookbook.model.room.fullRecipe.CaloriesNutrient
import com.example.cookbook.model.room.fullRecipe.CarbohydratesNutrient
import com.example.cookbook.model.room.fullRecipe.FatNutrient
import com.example.cookbook.model.room.fullRecipe.ProteinNutrient

@Entity(tableName = "Recipes_info_table", indices = [Index("id")])
data class RecipeInfoEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo(name = "recipe_title")
    val title: String,
    val image: String,
    val dairyFree: Int,
    val glutenFree: Int,
    val vegan: Int,
    val vegetarian: Int,
    val veryHealthy: Int,
    @TypeConverters(DataTypeConverters::class)
    val dishTypes: List<String>,
    val instructions: String,
    @Embedded
    val calories: CaloriesNutrient,
    @Embedded
    val fat: FatNutrient,
    @Embedded
    val carbohydrates: CarbohydratesNutrient,
    @Embedded
    val protein: ProteinNutrient,
    @Embedded
    val weightPerServing: WeightPerServing,
    val readyInMinutes: Int,
    val servings: Int,
    val sourceUrl: String,
    val summary: String,
    @TypeConverters(DataTypeConverters::class)
    val extendedIngredient: List<String>,
    @TypeConverters(DataTypeConverters::class)
    val analyzedInstruction: List<String>
)
