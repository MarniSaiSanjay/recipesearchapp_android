package com.example.recipesearchapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesearchapp.data.FavoriteRecipe
import com.example.recipesearchapp.model.Recipe
import com.example.recipesearchapp.model.RecipeResponse
import com.example.recipesearchapp.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for home screen managing recipe data with StateFlow.
 * Handles UI state including loading, success, error states, and favorites.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _allRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Favorites from Room database
    val favorites: StateFlow<List<FavoriteRecipe>> = repository.getAllFavorites()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Filtered recipes based on search query
    val filteredRecipes: StateFlow<List<Recipe>> = combine(
        _allRecipes,
        _searchQuery
    ) { recipes, query ->
        if (query.isBlank()) {
            recipes
        } else {
            recipes.filter { recipe ->
                recipe.title.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // For backwards compatibility with existing UI
    val recipes: StateFlow<RecipeResponse?> = _allRecipes
        .map { recipes ->
            if (recipes.isNotEmpty()) {
                RecipeResponse(recipes = recipes)
            } else {
                null
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleFavorite(recipe: Recipe) {
        viewModelScope.launch {
            try {
                val isFavorite = favorites.value.any { it.id == recipe.id }
                if (isFavorite) {
                    repository.removeFavorite(recipe.id)
                } else {
                    repository.addFavorite(recipe)
                }
            } catch (e: Exception) {
                _error.value = "Failed to update favorite: ${e.message}"
            }
        }
    }

    fun isFavorite(recipeId: Int): Boolean {
        return favorites.value.any { it.id == recipeId }
    }

    fun loadRandomRecipes(apiKey: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val result = repository.getRandomRecipes(apiKey)
                _allRecipes.value = result.recipes
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
