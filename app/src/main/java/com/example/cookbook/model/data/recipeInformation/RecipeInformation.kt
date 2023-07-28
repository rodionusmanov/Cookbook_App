package com.example.cookbook.model.data.recipeInformation

data class RecipeInformation(
    val analyzedInstructions: List<AnalyzedInstruction> = listOf(),
    val dairyFree: Boolean = false,
    val dishTypes: List<String> = listOf(),
    val extendedIngredients: List<ExtendedIngredient> = listOf(),
    val glutenFree: Boolean = false,
    val id: Int = 0,
    val image: String = "",
    val instructions: String = "",
    val calories: Nutrient? = null,
    val fat: Nutrient? = null,
    val carbohydrates: Nutrient? = null,
    val protein: Nutrient? = null,
    val weightPerServing: WeightPerServing = WeightPerServing(),
    val readyInMinutes: Int = 0,
    val servings: Int = 0,
    val sourceUrl: String = "",
    val summary: String = "",
    val title: String = "",
    val vegan: Boolean = false,
    val vegetarian: Boolean = false,
    val veryHealthy: Boolean = false
)