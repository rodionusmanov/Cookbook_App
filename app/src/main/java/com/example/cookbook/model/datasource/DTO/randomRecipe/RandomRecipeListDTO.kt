package com.example.cookbook.model.datasource.DTO.randomRecipe

import com.example.cookbook.model.domain.RandomRecipeData

data class RandomRecipeListDTO(
    val randomRecipeData: List<RandomRecipeData>
)