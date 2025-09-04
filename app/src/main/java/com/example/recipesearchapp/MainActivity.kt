package com.example.recipesearchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.recipesearchapp.components.*
import com.example.recipesearchapp.ui.theme.RecipeSearchAppTheme
import com.example.recipesearchapp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeSearchAppTheme {
                RecipeApp()
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
    var searchQuery by remember { mutableStateOf("") }
    
    // Use secure API key from BuildConfig
    val apiKey = BuildConfig.SPOONACULAR_API_KEY

    LaunchedEffect(Unit) {
        viewModel.loadRandomRecipes(apiKey)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
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
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { /* TODO: Implement search */ },
                modifier = Modifier.padding(horizontal = 0.dp)
            )
        }
        
        // Content Section
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Popular Recipes Section
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
            
            // All Recipes Section
            item {
                Text(
                    text = "All recipes",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                )
            }
            
            // All Recipes List
            when {
                recipes.value != null -> {
                    items(recipes.value!!.recipes) { recipe ->
                        RegularRecipeCard(
                            title = recipe.title,
                            readyInMinutes = "Ready in ${recipe.readyInMinutes ?: 25} min",
                            imageUrl = recipe.image,
                            isFavorite = false, // TODO: Implement favorite state
                            onFavoriteClick = { /* TODO: Implement favorite toggle */ },
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
fun FavoriteScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "❤️",
            fontSize = 48.sp
        )
        Text(
            text = "Favorite Recipes",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Your favorite recipes will appear here",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}