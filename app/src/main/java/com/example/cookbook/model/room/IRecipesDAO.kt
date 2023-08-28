package com.example.cookbook.model.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface IRecipesDAO {

    @Upsert(entity = RecipesEntity::class)
    suspend fun insertNewRecipe(recipeData: RecipesEntity)

    @Query("SELECT * FROM recipes_table")
    suspend fun getAllFavoriteRecipes(): List<RecipesEntity>

    @Query("DELETE FROM recipes_table WHERE id = :recipeId")
    suspend fun deleteRecipeFromFavorite(recipeId: Int)
}