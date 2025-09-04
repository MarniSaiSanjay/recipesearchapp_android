package com.example.recipesearchapp.network

import com.example.recipesearchapp.model.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API interface for Spoonacular recipe endpoints.
 * Defines network calls for fetching recipe data.
 */
interface RecipeApiService {

    // Example: Get random recipes
    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("apiKey") apiKey: String,
        @Query("number") number: Int = 10
    ): RecipeResponse
}
