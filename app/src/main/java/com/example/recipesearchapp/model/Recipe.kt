package com.example.recipesearchapp.model

/**
 * Data models for recipe-related API responses.
 * Contains Recipe and RecipeResponse classes for JSON parsing.
 */
data class RecipeResponse(
    val recipes: List<Recipe>
)

data class Recipe(
    val id: Int,
    val title: String,
    val image: String?,
    val readyInMinutes: Int?,
    val servings: Int?
)
