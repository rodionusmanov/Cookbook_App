package com.example.cookbook.utils

import com.example.cookbook.domain.Recipe
import com.example.cookbook.model.DTO.SearchRecipeListDTO

fun convertSearchDTOToRecipeList(searchRecipeListDTO: SearchRecipeListDTO): List<Recipe> {
    val recipeList = searchRecipeListDTO.searchRecipeList
    val returnRecipeList: MutableList<Recipe> = mutableListOf()
    recipeList.forEach {
        returnRecipeList.add(
            Recipe(
                it.id,
                it.title
            )
        )
    }
    return returnRecipeList
}