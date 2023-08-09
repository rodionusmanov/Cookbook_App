package com.example.cookbook.utils

import com.example.cookbook.model.datasource.DTO.recipeInformation.AnalyzedInstruction
import com.example.cookbook.model.datasource.DTO.recipeInformation.Equipment
import com.example.cookbook.model.datasource.DTO.recipeInformation.ExtendedIngredient
import com.example.cookbook.model.datasource.DTO.recipeInformation.Ingredient
import com.example.cookbook.model.datasource.DTO.recipeInformation.Nutrient
import com.example.cookbook.model.datasource.DTO.recipeInformation.RecipeInformationDTO
import com.example.cookbook.model.datasource.DTO.recipeInformation.Step
import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.model.domain.RandomRecipeData
import com.example.cookbook.model.domain.RecipeInformation
import com.example.cookbook.model.domain.SearchRecipeData
import java.lang.IllegalArgumentException

class MappingUtils {
    private fun mapAnalyzedInstructions(
        instructions: List<AnalyzedInstruction>
    ): List<AnalyzedInstruction> {
        return instructions.map { instruction ->
            AnalyzedInstruction(
                steps = instruction.steps.map { step ->
                    Step(
                        equipment = mapEquipment(step.equipment),
                        ingredients = mapIngredient(step.ingredients),
                        length = step.length,
                        number = step.number,
                        step = step.step
                    )
                }
            )
        }
    }

    fun mapRecipeInformation(
        dto: RecipeInformationDTO,
        nutritionMap: Map<String, Nutrient>
    ): RecipeInformation {
        return RecipeInformation(
            analyzedInstructions = mapAnalyzedInstructions(dto.analyzedInstructions),
            dairyFree = dto.dairyFree,
            dishTypes = dto.dishTypes,
            extendedIngredients = mapExtendedIngredients(dto.extendedIngredients),
            glutenFree = dto.glutenFree,
            id = dto.id,
            image = mapImageUrl(dto.image),
            instructions = dto.instructions,
            calories = extractNutritionInfo(nutritionMap, "Calories"),
            fat = extractNutritionInfo(nutritionMap, "Fat"),
            carbohydrates = extractNutritionInfo(nutritionMap, "Carbohydrates"),
            protein = extractNutritionInfo(nutritionMap, "Protein"),
            weightPerServing = dto.nutrition.weightPerServing,
            readyInMinutes = dto.readyInMinutes,
            servings = dto.servings,
            sourceUrl = dto.sourceUrl,
            summary = dto.summary,
            title = dto.title,
            vegan = dto.vegan,
            vegetarian = dto.vegetarian,
            veryHealthy = dto.veryHealthy
        )
    }

    private fun extractNutritionInfo(nutritionMap: Map<String, Nutrient>, key: String): Nutrient? {
        return nutritionMap[key]?.let {
            Nutrient(it.amount, it.name, it.percentOfDailyNeeds, it.unit)
        }
    }

    private fun mapIngredient(
        ingredients: List<Ingredient>
    ): List<Ingredient> {
        return ingredients.map { ingredient ->
            Ingredient(
                id = ingredient.id,
                image = BASE_INGREDIENT_IMAGE_URL + ingredient.image,
                name = ingredient.name
            )
        }
    }

    private fun mapEquipment(
        equipments: List<Equipment>
    ): List<Equipment> {
        return equipments.map { equipment ->
            Equipment(
                id = equipment.id,
                image = BASE_EQUIPMENT_IMAGE_URL + equipment.image,
                name = equipment.name
            )
        }
    }

    private fun mapExtendedIngredients(
        ingredients: List<ExtendedIngredient>
    ): List<ExtendedIngredient> {
        return ingredients.map { ingredient ->
            ExtendedIngredient(
                aisle = ingredient.aisle,
                id = ingredient.id,
                image = BASE_INGREDIENT_IMAGE_URL + ingredient.image,
                measures = ingredient.measures,
                meta = ingredient.meta,
                originalName = ingredient.originalName
            )
        }
    }

    fun mapRecipeData(recipe: BaseRecipeData): BaseRecipeData {
        val newImage = mapImageUrl(recipe.image)
        return when(recipe) {
            is RandomRecipeData -> recipe.copy(image = newImage)
            is SearchRecipeData -> recipe.copy(image = newImage)
            else -> throw IllegalArgumentException("Unidentified recipe type")
        }
    }

    private fun mapImageUrl(url:String): String {
        val newSize = "636x393"
        val patterns = listOf("90x90", "240x150", "312x150", "312x231", "480x360", "556x370")
        return patterns.fold(url){currentUrl, pattern ->
            currentUrl.replace(pattern, newSize)
        }
    }

    companion object {
        const val BASE_EQUIPMENT_IMAGE_URL = "https://spoonacular.com/cdn/equipment_100x100/"
        const val BASE_INGREDIENT_IMAGE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"
    }
}