package com.example.cookbook.model.repository.remoteDataSource

import com.example.cookbook.model.data.randomRecipe.Recipe
import com.example.cookbook.model.data.recipeInformation.AnalyzedInstruction
import com.example.cookbook.model.data.recipeInformation.Equipment
import com.example.cookbook.model.data.recipeInformation.ExtendedIngredient
import com.example.cookbook.model.data.recipeInformation.Ingredient
import com.example.cookbook.model.data.recipeInformation.Nutrient
import com.example.cookbook.model.data.recipeInformation.RecipeInformation
import com.example.cookbook.model.data.recipeInformation.Step
import com.example.cookbook.model.data.searchRecipe.SearchRecipeData
import com.example.cookbook.model.datasource.RandomRecipeDataSource
import com.example.cookbook.model.datasource.RecipeInformationDataSource
import com.example.cookbook.model.datasource.SearchRecipeDataSource
import retrofit2.Response

class SearchRepositoryImpl(
    private val searchRecipeDataSource: SearchRecipeDataSource,
    private val randomRecipeDataSource: RandomRecipeDataSource,
    private val recipeInformationDataSource: RecipeInformationDataSource
) : IRepositorySearchRequest {

    override suspend fun getSearchResult(
        request: String,
        ingredients: String
    ): List<SearchRecipeData> {

        val response = searchRecipeDataSource.getSearchResult(request, ingredients)
        return parseResponse(response) { it.searchRecipeList }
    }

    suspend fun getRandomRecipes(): List<Recipe> {

        val response = randomRecipeDataSource.getRandomRecipes()
        return parseResponse(response) { it.recipes }
    }

    suspend fun getRecipeInfo(id: Int): RecipeInformation {
        val response = recipeInformationDataSource.getRecipeFullInformation(id)

        if (response.isSuccessful) {
            val dto = response.body()
            if (dto != null) {
                val nutritionMap = dto.nutrition.nutrients.associateBy { it.name }
                return RecipeInformation(
                    analyzedInstructions = mapAnalyzedInstructions(dto.analyzedInstructions),
                    dairyFree = dto.dairyFree,
                    dishTypes = dto.dishTypes,
                    extendedIngredients = mapExtendedIngredients(dto.extendedIngredients),
                    glutenFree = dto.glutenFree,
                    id = dto.id,
                    image = dto.image,
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
            } else {
                throw Exception("Response body is null")
            }
        } else {
            throw Exception("Response was not successful: ${response.message()}")
        }
    }

    private fun <T, R> parseResponse(response: Response<T>, dataSelector: (T) -> R): R {
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return dataSelector(body)
            } else {
                throw Exception("Response body is null")
            }
        } else {
            throw Exception("Response was not successful: ${response.message()}")
        }
    }

    private fun extractNutritionInfo(nutritionMap: Map<String, Nutrient>, key: String): Nutrient? {
        return nutritionMap[key]?.let {
            Nutrient(it.amount, it.name, it.percentOfDailyNeeds, it.unit)
        }
    }

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

    private fun mapIngredient(
        ingredients: List<Ingredient>
    ): List<Ingredient> {
        return ingredients.map { ingredient ->
            Ingredient(
                id = ingredient.id,
                image = BASE_EQUIPMENT_IMAGE_URL + ingredient.image,
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

    companion object {
        const val BASE_EQUIPMENT_IMAGE_URL = "https://spoonacular.com/cdn/equipment_100x100/"
        const val BASE_INGREDIENT_IMAGE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"
    }
}