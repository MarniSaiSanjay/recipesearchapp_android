package com.example.recipesearchapp.repository

import com.example.recipesearchapp.model.RecipeResponse
import com.example.recipesearchapp.network.RecipeApiService
import javax.inject.Inject

/**
 * Repository class implementing Repository pattern for recipe data.
 * Acts as single source of truth between ViewModel and API service.
 * 
 * Simple explanation: Repository pulls data from APIService by calling APIs 
 * and then gives it to the ViewModel.
 */
class RecipeRepository @Inject constructor(
    private val apiService: RecipeApiService
) {
    suspend fun getRandomRecipes(apiKey: String): RecipeResponse {
        return apiService.getRandomRecipes(apiKey)
    }
}
