package com.example.cookbook.model.room.fullRecipe

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface IRecipesInfoDAO {

    @Upsert(entity = RecipeInfoEntity::class)
    suspend fun upsertRecipeToFavorite(recipeData: RecipeInfoEntity)

    @Query("SELECT * FROM Recipes_info_table")
    fun getAllFavoriteRecipes(): Flow<List<RecipeInfoEntity>>

    @Query("SELECT * FROM Recipes_info_table WHERE id = :recipeId")
    suspend fun getFavoriteRecipeById(recipeId: Int): RecipeInfoEntity

    @Query("DELETE FROM Recipes_info_table WHERE id = :recipeId")
    suspend fun deleteRecipeFromFavorite(recipeId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM Recipes_info_table WHERE id = :recipeId)")
    suspend fun exists(recipeId: Int): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM Recipes_info_table WHERE id = :recipeId)")
    fun observeExistence(recipeId: Int): Flow<Boolean>
}