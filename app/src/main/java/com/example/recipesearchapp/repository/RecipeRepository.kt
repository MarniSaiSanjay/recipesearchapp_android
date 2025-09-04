package com.example.recipesearchapp.repository

import com.example.recipesearchapp.data.FavoriteRecipe
import com.example.recipesearchapp.data.FavoriteRecipeDao
import com.example.recipesearchapp.model.Recipe
import com.example.recipesearchapp.model.RecipeResponse
import com.example.recipesearchapp.network.RecipeApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repository class implementing Repository pattern for recipe data.
 * Acts as single source of truth between ViewModel and data sources (API + Database).
 * 
 * Simple explanation: Repository pulls data from APIService and manages local favorites
 * in Room database, then provides unified data to the ViewModel.
 */
class RecipeRepository @Inject constructor(
    private val apiService: RecipeApiService,
    private val favoriteDao: FavoriteRecipeDao
) {
    // Network operations
    suspend fun getRandomRecipes(apiKey: String): RecipeResponse {
        return apiService.getRandomRecipes(apiKey)
    }

    // Local database operations for favorites
    fun getAllFavorites(): Flow<List<FavoriteRecipe>> {
        return favoriteDao.getAllFavorites()
    }

    suspend fun addFavorite(recipe: Recipe) {
        val favoriteRecipe = FavoriteRecipe(
            id = recipe.id,
            title = recipe.title,
            image = recipe.image,
            readyInMinutes = recipe.readyInMinutes,
            servings = recipe.servings
        )
        favoriteDao.insert(favoriteRecipe)
    }

    suspend fun removeFavorite(recipeId: Int) {
        favoriteDao.deleteById(recipeId)
    }

    suspend fun isFavorite(recipeId: Int): Boolean {
        return favoriteDao.getFavoriteById(recipeId) != null
    }

    suspend fun getFavoriteCount(): Int {
        return favoriteDao.getFavoriteCount()
    }
}
