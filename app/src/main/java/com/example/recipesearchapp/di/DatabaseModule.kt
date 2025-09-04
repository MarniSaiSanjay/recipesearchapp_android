package com.example.recipesearchapp.di

import android.content.Context
import androidx.room.Room
import com.example.recipesearchapp.data.FavoriteRecipeDao
import com.example.recipesearchapp.data.RecipeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt dependency injection module for Room database components.
 * Provides database and DAO instances for the entire app.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRecipeDatabase(@ApplicationContext context: Context): RecipeDatabase {
        return Room.databaseBuilder(
            context,
            RecipeDatabase::class.java,
            RecipeDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideFavoriteRecipeDao(database: RecipeDatabase): FavoriteRecipeDao {
        return database.favoriteRecipeDao()
    }
}
