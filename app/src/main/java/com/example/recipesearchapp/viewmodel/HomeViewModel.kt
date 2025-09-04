package com.example.recipesearchapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesearchapp.model.Recipe
import com.example.recipesearchapp.model.RecipeResponse
import com.example.recipesearchapp.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for home screen managing recipe data with StateFlow.
 * Handles UI state including loading, success, and error states.
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
    }.let { flow ->
        MutableStateFlow(emptyList<Recipe>()).apply {
            viewModelScope.launch {
                flow.collect { value = it }
            }
        }
    }

    // For backwards compatibility with existing UI
    val recipes: StateFlow<RecipeResponse?> = combine(
        filteredRecipes,
        _isLoading
    ) { filtered, loading ->
        if (!loading && filtered.isNotEmpty()) {
            RecipeResponse(recipes = filtered)
        } else if (!loading && _allRecipes.value.isNotEmpty()) {
            RecipeResponse(recipes = _allRecipes.value)
        } else {
            null
        }
    }.let { flow ->
        MutableStateFlow<RecipeResponse?>(null).apply {
            viewModelScope.launch {
                flow.collect { value = it }
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
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
