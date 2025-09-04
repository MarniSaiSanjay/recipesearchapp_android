package com.example.recipesearchapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for favorite recipes.
 * Defines database operations for the favorites feature.
 */
@Dao
interface FavoriteRecipeDao {
    
    @Query("SELECT * FROM favorite_recipes ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteRecipe>>
    
    @Query("SELECT * FROM favorite_recipes WHERE id = :recipeId")
    suspend fun getFavoriteById(recipeId: Int): FavoriteRecipe?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: FavoriteRecipe)
    
    @Delete
    suspend fun delete(recipe: FavoriteRecipe)
    
    @Query("DELETE FROM favorite_recipes WHERE id = :recipeId")
    suspend fun deleteById(recipeId: Int)
    
    @Query("SELECT COUNT(*) FROM favorite_recipes")
    suspend fun getFavoriteCount(): Int
}
