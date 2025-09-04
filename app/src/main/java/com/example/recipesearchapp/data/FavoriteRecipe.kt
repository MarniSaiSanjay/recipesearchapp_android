package com.example.recipesearchapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for storing favorite recipes locally.
 * This allows users to save recipes they like for offline access.
 */
@Entity(tableName = "favorite_recipes")
data class FavoriteRecipe(
    @PrimaryKey
    val id: Int,
    val title: String,
    val image: String?,
    val readyInMinutes: Int? = null,
    val servings: Int? = null,
    val addedAt: Long = System.currentTimeMillis() // Timestamp when added to favorites
)
