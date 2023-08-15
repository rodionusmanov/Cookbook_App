package com.example.cookbook.model.room

import androidx.room.BuiltInTypeConverters
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cookbook.model.room.fullRecipe.IRecipesInfoDAO
import com.example.cookbook.model.room.fullRecipe.RecipeInfoEntity

@Database(
    version = 1,
    entities = [
        RecipeInfoEntity::class
    ]
)
@TypeConverters(
    DataTypeConverters::class,
    builtInTypeConverters = BuiltInTypeConverters(enums = BuiltInTypeConverters.State.DISABLED)
)
abstract class RecipesDatabase : RoomDatabase() {
    abstract fun getRecipesDAO(): IRecipesInfoDAO
}