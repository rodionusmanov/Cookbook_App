package com.example.cookbook.utils

import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.model.domain.SearchRecipeData
import com.example.cookbook.model.room.RecipesEntity

fun convertRecipeEntityToList(entityList: List<RecipesEntity>): List<SearchRecipeData> {
    return entityList.map {
        SearchRecipeData(it.id, it.title, it.image)
    }
}

fun convertRecipeToEntity(recipe: BaseRecipeData): RecipesEntity {
    return RecipesEntity(
        recipe.id,
        recipe.title,
        recipe.image
    )
}

