package com.example.recipesearchapp.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

/**
 * Room database for the Recipe Search App.
 * Contains the database holder and serves as the main access point
 * for the underlying connection to your app's persisted data.
 */
@Database(
    entities = [FavoriteRecipe::class],
    version = 1,
    exportSchema = false
)
abstract class RecipeDatabase : RoomDatabase() {
    
    abstract fun favoriteRecipeDao(): FavoriteRecipeDao
    
    companion object {
        const val DATABASE_NAME = "recipe_database"
    }
}
