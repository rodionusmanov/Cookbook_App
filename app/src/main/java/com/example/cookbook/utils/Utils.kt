package com.example.cookbook.utils

import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.model.domain.SearchRecipeData
import com.example.cookbook.model.room.RecipesEntity

fun convertRecipeEntityToList(entityList: List<RecipesEntity>): List<BaseRecipeData> {
    return entityList.map {entity ->
       SearchRecipeData(entity.id, entity.title, entity.image)
    }
}

fun convertRecipeToEntity(recipeData: BaseRecipeData): RecipesEntity {
    return RecipesEntity(recipeData.id, recipeData.title, recipeData.image)
}

