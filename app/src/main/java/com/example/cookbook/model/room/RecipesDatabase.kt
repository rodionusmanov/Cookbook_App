package com.example.cookbook.model.room

import androidx.room.RoomDatabase

abstract class RecipesDatabase: RoomDatabase() {
    abstract fun getRecipesDAO(): IRecipesDAO
}