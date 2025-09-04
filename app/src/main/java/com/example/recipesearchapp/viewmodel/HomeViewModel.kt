package com.example.recipesearchapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesearchapp.model.RecipeResponse
import com.example.recipesearchapp.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _recipes = MutableStateFlow<RecipeResponse?>(null)
    val recipes: StateFlow<RecipeResponse?> = _recipes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadRandomRecipes(apiKey: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val result = repository.getRandomRecipes(apiKey)
                _recipes.value = result
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
