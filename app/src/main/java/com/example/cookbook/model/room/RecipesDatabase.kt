package com.example.cookbook.model.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [RecipesEntity::class]
)
abstract class RecipesDatabase : RoomDatabase() {
    abstract fun getRecipesDAO(): IRecipesDAO
}