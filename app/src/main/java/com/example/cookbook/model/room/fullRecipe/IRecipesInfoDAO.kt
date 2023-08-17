package com.example.cookbook.model.room.fullRecipe

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface IRecipesInfoDAO {

    @Upsert(entity = RecipeInfoEntity::class)
    suspend fun upsertRecipeToFavorite(recipeData: RecipeInfoEntity)

    @Query("SELECT * FROM Recipes_info_table")
    suspend fun getAllFavoriteRecipes(): List<RecipeInfoEntity>

    @Query("SELECT * FROM Recipes_info_table WHERE id = :recipeId")
    suspend fun getFavoriteRecipeById(recipeId: Int): RecipeInfoEntity

    @Query("DELETE FROM Recipes_info_table WHERE id = :recipeId")
    suspend fun deleteRecipeFromFavorite(recipeId: Int)
}