package com.example.cookbook.utils

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
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

inline fun <reified T : Parcelable> Bundle.parcelableArrayList(key: String): ArrayList<T>? =
    when {
        SDK_INT >= 33 -> getParcelableArrayList(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableArrayList(key)
    }

