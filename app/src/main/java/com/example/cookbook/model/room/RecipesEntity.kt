package com.example.cookbook.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "recipes_table", indices = [Index("id")])
data class RecipesEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo(name = "recipe_title")
    val title: String,
    val image: String
)