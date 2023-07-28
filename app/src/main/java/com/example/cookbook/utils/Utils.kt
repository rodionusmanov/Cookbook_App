package com.example.cookbook.utils

import com.example.cookbook.model.data.randomRecipe.Recipe
import com.example.cookbook.model.data.searchRecipe.SearchRecipeListDTO

fun convertSearchDTOToRecipeList(searchRecipeListDTO: SearchRecipeListDTO): List<Recipe> {
    val recipeList = searchRecipeListDTO.searchRecipeList
    val returnRecipeList: MutableList<Recipe> = mutableListOf()
    recipeList.forEach {
        returnRecipeList.add(
            Recipe(
                it.id,
                it.title,
                it.image
            )
        )
    }
    return returnRecipeList
}