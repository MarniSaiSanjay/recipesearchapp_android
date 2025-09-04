package com.example.recipesearchapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for Hilt dependency injection setup.
 * Entry point for Hilt to generate dependency injection components.
 */
@HiltAndroidApp
class RecipeSearchApp : Application()
