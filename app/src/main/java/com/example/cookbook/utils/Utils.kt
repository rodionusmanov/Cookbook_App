package com.example.cookbook.utils

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import com.example.cookbook.model.datasource.DTO.recipeInformation.AnalyzedInstruction
import com.example.cookbook.model.datasource.DTO.recipeInformation.Equipment
import com.example.cookbook.model.datasource.DTO.recipeInformation.ExtendedIngredient
import com.example.cookbook.model.datasource.DTO.recipeInformation.Ingredient
import com.example.cookbook.model.datasource.DTO.recipeInformation.Measures
import com.example.cookbook.model.datasource.DTO.recipeInformation.Metric
import com.example.cookbook.model.datasource.DTO.recipeInformation.Nutrient
import com.example.cookbook.model.datasource.DTO.recipeInformation.Step
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
        convertExtendedIngredientToStringList(recipe.extendedIngredients),
        convertAnalyzedInstructionToStringList(recipe.analyzedInstructions)
    )
}

fun convertAnalyzedInstructionToStringList(analyzedInstructions: List<AnalyzedInstruction>): List<String> {
    val resultList: MutableList<String> = mutableListOf()
    analyzedInstructions.forEach {
        resultList.add(
            convertStepsToString(it.steps)
        )
    }
    return resultList
}

fun convertStepsToString(steps: List<Step>): String {
    val resultList: MutableList<String> = mutableListOf()
    steps.forEach {
        resultList.add(
            listOf<String>(
                convertEquipmentListToString(it.equipment),
                convertIngredientsListToString(it.ingredients),
                it.step
            ).joinToString(separator = SEPARATOR_STEPS)
        )
    }
    return resultList.joinToString(separator = SEPARATOR_STEPS_LIST)
}

fun convertIngredientsListToString(ingredients: List<Ingredient>): String {
    val resultList: MutableList<String> = mutableListOf()
    ingredients.forEach {
        resultList.add(
            listOf<String>(
                it.image,
                it.name
            ).joinToString(separator = SEPARATOR_INGREDIENT)
        )
    }
    return resultList.joinToString(separator = SEPARATOR_INGREDIENT_LIST)
}

fun convertEquipmentListToString(equipment: List<Equipment>): String {
    val resultList: MutableList<String> = mutableListOf()
    equipment.forEach {
        resultList.add(
            listOf<String>(
                it.image,
                it.name
            ).joinToString(separator = SEPARATOR_EQUIPMENT)
        )
    }
    return resultList.joinToString(separator = SEPARATOR_EQUIPMENT_LIST)
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

fun convertRecipeInfoEntityToRecipeInformation(entity: RecipeInfoEntity): RecipeInformation {
    return RecipeInformation(
        convertStringListToAnalyzedInstruction(entity.analyzedInstruction),
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

fun convertStringListToAnalyzedInstruction(analyzedInstruction: List<String>): List<AnalyzedInstruction> {
    val resultList: MutableList<AnalyzedInstruction> = mutableListOf()
    var equipmentList: MutableList<Equipment> = mutableListOf()
    var ingredientList: MutableList<Ingredient> = mutableListOf()
    var stepName = ""

    analyzedInstruction.forEach { instruction ->
        val stepsString = instruction.split(SEPARATOR_STEPS_LIST).toTypedArray()
        val stepList: MutableList<Step> = mutableListOf()

        stepsString.forEach { stepString ->
            val stepElements = stepString.split(SEPARATOR_STEPS)

            val equipmentInnerString =
                stepElements[0].split(SEPARATOR_EQUIPMENT_LIST).toTypedArray()
            val ingredientInnerString =
                stepElements[1].split(SEPARATOR_INGREDIENT_LIST).toTypedArray()
            stepName = stepElements[2]

            equipmentInnerString.forEach { innerEquipment ->
                val equipmentStrings = innerEquipment.split(SEPARATOR_EQUIPMENT).toTypedArray()
                equipmentList.add(
                    if (innerEquipment != "") {
                        Equipment(
                            0,
                            equipmentStrings[0],
                            equipmentStrings[1]
                        )
                    } else {
                        Equipment(0, "", "")
                    }

                )
            }

            ingredientInnerString.forEach { innerIngredient ->
                val ingredientStrings =
                    innerIngredient.split(SEPARATOR_INGREDIENT).toTypedArray()
                ingredientList.add(
                    if (innerIngredient != "") {
                        Ingredient(
                            0,
                            ingredientStrings[0],
                            ingredientStrings[1]
                        )
                    } else {
                        Ingredient(
                            0, "", ""
                        )
                    }
                )
            }

            stepList.add(
                Step(
                    equipmentList,
                    ingredientList,
                    null,
                    0,
                    stepName
                )
            )
            stepName = ""
        }
        resultList.add(
            AnalyzedInstruction(stepList)
        )
    }
    return resultList
}


inline fun <reified T : Parcelable> Bundle.parcelableArrayList(key: String): ArrayList<T>? =
    when {
        SDK_INT >= 33 -> getParcelableArrayList(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableArrayList(key)
    }

