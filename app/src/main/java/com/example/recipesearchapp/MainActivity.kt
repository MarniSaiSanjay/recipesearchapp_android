package com.example.recipesearchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.recipesearchapp.BuildConfig
import com.example.recipesearchapp.components.*
import com.example.recipesearchapp.ui.theme.RecipeSearchAppTheme
import com.example.recipesearchapp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContent {
                RecipeSearchAppTheme {
                    RecipeApp()
                }
            }
        } catch (e: Exception) {
            // Log the error and show a basic fallback UI
            e.printStackTrace()
            setContent {
                Text(
                    text = "Error loading app: ${e.message}",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeApp() {
    var currentRoute by remember { mutableStateOf("home") }
    
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onNavigate = { route -> currentRoute = route }
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        when (currentRoute) {
            "home" -> HomeScreen(
                modifier = Modifier.padding(paddingValues)
            )
            "favorite" -> FavoriteScreen(
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val recipes = viewModel.recipes.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val error = viewModel.error.collectAsState()
    val searchQuery = viewModel.searchQuery.collectAsState()
    val filteredRecipes = viewModel.filteredRecipes.collectAsState()
    val favorites = viewModel.favorites.collectAsState()
    val focusManager = LocalFocusManager.current
    
    // Use secure API key from BuildConfig
    val apiKey = try {
        BuildConfig.SPOONACULAR_API_KEY.takeIf { it.isNotEmpty() } ?: "YOUR_API_KEY_HERE"
    } catch (e: Exception) {
        "YOUR_API_KEY_HERE"
    }

    LaunchedEffect(Unit) {
        if (apiKey != "YOUR_API_KEY_HERE") {
            viewModel.loadRandomRecipes(apiKey)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            }
    ) {
        // Header Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            // Greeting
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "👋",
                            fontSize = 24.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "Hey Name",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                    Text(
                        text = "Discover tasty and healthy receipt",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Search Bar
            SearchBar(
                query = searchQuery.value,
                onQueryChange = { viewModel.updateSearchQuery(it) },
                onSearch = { /* Search is reactive, no action needed */ },
                modifier = Modifier.padding(horizontal = 0.dp)
            )
        }
        
        // Content Section
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    focusManager.clearFocus()
                },
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Popular Recipes Section - Hide when searching
            if (searchQuery.value.isBlank()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp)
                    ) {
                        Text(
                            text = "Popular Recipes",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    
                    when {
                        isLoading.value -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        error.value != null -> {
                            Text(
                                text = "Error loading recipes",
                                color = Color.Red,
                                modifier = Modifier.padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        recipes.value != null -> {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(recipes.value!!.recipes.take(5)) { recipe ->
                                    PopularRecipeCard(
                                        title = recipe.title,
                                        readyInMinutes = "Ready in ${recipe.readyInMinutes ?: 25} min",
                                        imageUrl = recipe.image,
                                        onClick = { /* TODO: Navigate to recipe detail */ }
                                    )
                                }
                            }
                        }
                        else -> {
                            Text(
                                text = "Loading recipes...",
                                modifier = Modifier.padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            }
            
            // All Recipes Section
            item {
                val displayText = if (searchQuery.value.isBlank()) {
                    "All recipes"
                } else {
                    "Search results (${filteredRecipes.value.size})"
                }
                
                Text(
                    text = displayText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                )
            }
            
            // All Recipes List - Show filtered results
            when {
                isLoading.value -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                filteredRecipes.value.isEmpty() && searchQuery.value.isNotBlank() -> {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🔍",
                                fontSize = 48.sp
                            )
                            Text(
                                text = "No recipes found",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                            Text(
                                text = "Try searching for something else",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 8.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                filteredRecipes.value.isNotEmpty() -> {
                    items(filteredRecipes.value) { recipe ->
                        val isFavorite = favorites.value.any { it.id == recipe.id }
                        RegularRecipeCard(
                            title = recipe.title,
                            readyInMinutes = "Ready in ${recipe.readyInMinutes ?: 25} min",
                            imageUrl = recipe.image,
                            isFavorite = isFavorite,
                            onFavoriteClick = { viewModel.toggleFavorite(recipe) },
                            onClick = { /* TODO: Navigate to recipe detail */ },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val favorites = viewModel.favorites.collectAsState()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = "❤️ Favorite Recipes",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "${favorites.value.size} recipes saved",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        
        // Content
        if (favorites.value.isEmpty()) {
            // Empty state
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "💔",
                    fontSize = 48.sp
                )
                Text(
                    text = "No Favorite Recipes",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = "Start adding recipes to your favorites by tapping the heart icon!",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            // Favorites list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favorites.value) { favorite ->
                    RegularRecipeCard(
                        title = favorite.title,
                        readyInMinutes = "Ready in ${favorite.readyInMinutes ?: 25} min",
                        imageUrl = favorite.image,
                        isFavorite = true,
                        onFavoriteClick = { 
                            // Convert FavoriteRecipe back to Recipe for toggleFavorite
                            val recipe = com.example.recipesearchapp.model.Recipe(
                                id = favorite.id,
                                title = favorite.title,
                                image = favorite.image,
                                readyInMinutes = favorite.readyInMinutes,
                                servings = favorite.servings
                            )
                            viewModel.toggleFavorite(recipe)
                        },
                        onClick = { /* TODO: Navigate to recipe detail */ }
                    )
                }
            }
        }
    }
}