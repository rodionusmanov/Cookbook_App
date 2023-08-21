package com.example.cookbook.utils

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import com.example.cookbook.model.datasource.DTO.recipeInformation.AnalyzedInstruction
import com.example.cookbook.model.datasource.DTO.recipeInformation.ExtendedIngredient
import com.example.cookbook.model.datasource.DTO.recipeInformation.Measures
import com.example.cookbook.model.datasource.DTO.recipeInformation.Metric
import com.example.cookbook.model.datasource.DTO.recipeInformation.Nutrient
import com.example.cookbook.model.datasource.DTO.recipeInformation.WeightPerServing
import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.model.domain.RecipeInformation
import com.example.cookbook.model.domain.SearchRecipeData
import com.example.cookbook.model.room.RecipesEntity
import com.example.cookbook.model.room.fullRecipe.CaloriesNutrient
import com.example.cookbook.model.room.fullRecipe.CarbohydratesNutrient
import com.example.cookbook.model.room.fullRecipe.FatNutrient
import com.example.cookbook.model.room.fullRecipe.ProteinNutrient
import com.example.cookbook.model.room.fullRecipe.RecipeInfoEntity

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
        recipe.dishTypes,
        recipe.instructions,
        CaloriesNutrient(
            recipe.calories!!.amount,
            recipe.calories.name,
            recipe.calories.percentOfDailyNeeds,
            recipe.calories.unit
        ),
        FatNutrient(
            recipe.fat!!.amount,
            recipe.fat.name,
            recipe.fat.percentOfDailyNeeds,
            recipe.fat.unit
        ),
        CarbohydratesNutrient(
            recipe.carbohydrates!!.amount,
            recipe.carbohydrates.name,
            recipe.carbohydrates.percentOfDailyNeeds,
            recipe.carbohydrates.unit
        ),
        ProteinNutrient(
            recipe.protein!!.amount,
            recipe.protein.name,
            recipe.protein.percentOfDailyNeeds,
            recipe.protein.unit
        ),
        recipe.weightPerServing,
        recipe.readyInMinutes,
        recipe.servings,
        recipe.sourceUrl,
        recipe.summary,
        convertExtendedIngredientToStringList(recipe.extendedIngredients)
    )
}

fun convertExtendedIngredientToStringList(extendedIngredients: List<ExtendedIngredient>): List<String> {
    val resultList: MutableList<String> = mutableListOf()
    extendedIngredients.forEach {
        resultList.add(
            listOf<String>(
                it.originalName,
                it.measures.metric.amount.toString(),
                it.measures.metric.unitLong
            ).joinToString(
                separator = SEPARATOR
            )
        )
    }
    return resultList
}

fun convertRecipeInfoEntityToList(entityList: List<RecipeInfoEntity>): List<RecipeInformation> {
    return entityList.map {
        RecipeInformation(
            emptyList<AnalyzedInstruction>(),
            (it.dairyFree == 1),
            it.dishTypes,
            convertStringListToExtendedIngredients(it.extendedIngredient),
            (it.glutenFree == 1),
            it.id,
            it.image,
            it.instructions,
            Nutrient(
                it.calories.caloriesAmount,
                it.calories.caloriesName,
                it.calories.caloriesPercentOfDailyNeeds,
                it.calories.caloriesUnit,
            ),
            Nutrient(
                it.fat.fatAmount,
                it.fat.fatName,
                it.fat.fatPercentOfDailyNeeds,
                it.fat.fatUnit,
            ),
            Nutrient(
                it.carbohydrates.carbohydratesAmount,
                it.carbohydrates.carbohydratesName,
                it.carbohydrates.carbohydratesPercentOfDailyNeeds,
                it.carbohydrates.carbohydratesUnit,
            ),
            Nutrient(
                it.protein.proteinAmount,
                it.protein.proteinName,
                it.protein.proteinPercentOfDailyNeeds,
                it.protein.proteinUnit,
            ),
            it.weightPerServing,
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

fun convertStringListToExtendedIngredients(extendedIngredient: List<String>): List<ExtendedIngredient> {
    val resultList: MutableList<ExtendedIngredient> = mutableListOf()
    extendedIngredient.forEach {
        val strings = it.split(SEPARATOR).toTypedArray()
        resultList.add(
            ExtendedIngredient(
                "",
                0,
                "",
                Measures(Metric(strings[1].toDouble(), strings[2], "")),
                listOf(),
                strings[0]
            )
        )
    }
    return resultList
}

fun convertRecipeInfoEntityToRecipeInformation(entity: RecipeInfoEntity): RecipeInformation {
    return RecipeInformation(
        emptyList<AnalyzedInstruction>(),
        (entity.dairyFree == 1),
        entity.dishTypes,
        convertStringListToExtendedIngredients(entity.extendedIngredient),
        (entity.glutenFree == 1),
        entity.id,
        entity.image,
        entity.instructions,
        Nutrient(
            entity.calories.caloriesAmount,
            entity.calories.caloriesName,
            entity.calories.caloriesPercentOfDailyNeeds,
            entity.calories.caloriesUnit,
        ),
        Nutrient(
            entity.fat.fatAmount,
            entity.fat.fatName,
            entity.fat.fatPercentOfDailyNeeds,
            entity.fat.fatUnit,
        ),
        Nutrient(
            entity.carbohydrates.carbohydratesAmount,
            entity.carbohydrates.carbohydratesName,
            entity.carbohydrates.carbohydratesPercentOfDailyNeeds,
            entity.carbohydrates.carbohydratesUnit,
        ),
        Nutrient(
            entity.protein.proteinAmount,
            entity.protein.proteinName,
            entity.protein.proteinPercentOfDailyNeeds,
            entity.protein.proteinUnit,
        ),
        entity.weightPerServing,
        entity.readyInMinutes,
        entity.servings,
        entity.sourceUrl,
        entity.summary,
        entity.title,
        (entity.vegan == 1),
        (entity.vegetarian == 1),
        (entity.veryHealthy == 1)
    )

}

fun convertDishTypesListToString(dishTypes: List<String>): String {
    val resultString: StringBuilder = java.lang.StringBuilder()
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
    val resultString: StringBuilder = java.lang.StringBuilder()
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
    val resultString: StringBuilder = java.lang.StringBuilder()
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

