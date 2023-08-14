package com.example.cookbook.utils

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import com.example.cookbook.model.datasource.DTO.recipeInformation.AnalyzedInstruction
import com.example.cookbook.model.datasource.DTO.recipeInformation.ExtendedIngredient
import com.example.cookbook.model.datasource.DTO.recipeInformation.Nutrient
import com.example.cookbook.model.datasource.DTO.recipeInformation.WeightPerServing
import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.model.domain.RecipeInformation
import com.example.cookbook.model.domain.SearchRecipeData
import com.example.cookbook.model.room.RecipeInfoEntity
import com.example.cookbook.model.room.RecipesEntity
import java.lang.StringBuilder

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

fun convertRecipeInfoToEntity(recipe: RecipeInformation): RecipeInfoEntity {
    return RecipeInfoEntity(
        recipe.id,
        recipe.title,
        recipe.image,
        if (recipe.dairyFree) {
            1
        } else {
            0
        },
        if (recipe.glutenFree) {
            1
        } else {
            0
        },
        if (recipe.vegan) {
            1
        } else {
            0
        },
        if (recipe.vegetarian) {
            1
        } else {
            0
        },
        if (recipe.veryHealthy) {
            1
        } else {
            0
        },
        convertDishTypesListToString(recipe.dishTypes),
        recipe.instructions,
        convertNutrientToString(recipe.calories!!),
        convertNutrientToString(recipe.fat!!),
        convertNutrientToString(recipe.carbohydrates!!),
        convertNutrientToString(recipe.protein!!),
        convertWeightPerServingToString(recipe.weightPerServing),
        recipe.readyInMinutes,
        recipe.servings,
        recipe.sourceUrl,
        recipe.summary
    )
}

fun convertRecipeInfoEntityToList(entityList: List<RecipeInfoEntity>): List<RecipeInformation> {
    return entityList.map {
        RecipeInformation(
            emptyList<AnalyzedInstruction>(),
            (it.dairyFree == 1),
            convertStringToDishTypes(it.dishTypes),
            emptyList<ExtendedIngredient>(),
            (it.glutenFree == 1),
            it.id,
            it.image,
            it.instructions,
            convertStringToNutrient(it.calories),
            convertStringToNutrient(it.fat),
            convertStringToNutrient(it.carbohydrates),
            convertStringToNutrient(it.protein),
            convertStringToWeightPerServing(it.weightPerServing),
            it.readyInMinutes,
            it.servings,
            it.sourceUrl,
            it.summary,
            it.title,
            (it.vegan == 1),
            (it.vegetarian == 1),
            (it.veryHealthy == 1)
        )
    }
}

fun convertDishTypesListToString(dishTypes: List<String>): String {
    var resultString: StringBuilder = java.lang.StringBuilder()
    var firstInList = true
    for (s in dishTypes) {
        if (!firstInList) resultString.append("~~")
        resultString.append(s)
        firstInList = false
    }
    return resultString.toString()
}


fun convertStringToDishTypes(string: String): List<String> {
    return string.trim().splitToSequence("~~")
        .filter { it.isNotEmpty() }
        .toList()
}

fun convertNutrientToString(nutrient: Nutrient): String {
    var resultString: StringBuilder = java.lang.StringBuilder()
    resultString.append(nutrient.amount.toString()).append("~~")
        .append(nutrient.name).append("~~")
        .append(nutrient.percentOfDailyNeeds.toString()).append("~~")
        .append(nutrient.unit)
    return resultString.toString()
}

fun convertStringToNutrient(string: String): Nutrient {
    val stringList: List<String> = string.trim().splitToSequence("~~")
        .filter { it.isNotEmpty() }
        .toList()
    return Nutrient(
        stringList[0].toDouble(),
        stringList[1],
        stringList[2].toDouble(),
        stringList[3]
    )
}

fun convertWeightPerServingToString(wps: WeightPerServing): String {
    var resultString: StringBuilder = java.lang.StringBuilder()
    resultString.append(wps.amount.toString()).append("~~")
        .append(wps.unit)
    return resultString.toString()
}

fun convertStringToWeightPerServing(string: String): WeightPerServing {
    val stringList: List<String> = string.trim().splitToSequence("~~")
        .filter { it.isNotEmpty() }
        .toList()
    return WeightPerServing(
        stringList[0].toInt(),
        stringList[1]
    )
}

inline fun <reified T : Parcelable> Bundle.parcelableArrayList(key: String): ArrayList<T>? =
    when {
        SDK_INT >= 33 -> getParcelableArrayList(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableArrayList(key)
    }

