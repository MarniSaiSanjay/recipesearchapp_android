# Recipe Search App 🍽️

A comprehensive Android app for discovering and managing recipes using the **Spoonacular API**. Built with modern Android technologies including **Jetpack Compose**, **Hilt**, **MVVM architecture**, and **Room database**.

## 📋 About This App

This recipe search app allows users to:
- **Browse popular recipes** from Spoonacular's database
- **Search for specific recipes** by name or ingredients
- **View detailed recipe information** including ingredients and instructions
- **Save favorite recipes** locally for offline access
- **Manage favorite recipes** with local caching using Room database

### 🎨 Design Reference
- **Figma Design**: [Android Onboarding Assignment](https://www.figma.com/design/MpLnfA9X2cP2C9SBb1LDAd/Android-Onboarding-Assignment?node-id=0-1&p=f)
- **API Documentation**: [Spoonacular Food API](https://spoonacular.com/food-api/docs)

## 🚀 Quick Start Guide

### Step 1: Install Android Studio
1. Download [Android Studio](https://developer.android.com/studio) (latest version)
2. Install with default settings
3. Open Android Studio and complete the setup wizard

### Step 2: Clone & Open Project
```bash
git clone https://github.com/MarniSaiSanjay/recipesearchapp_android.git
```
- Open Android Studio → **Open** → Select the cloned folder
- Wait for Gradle sync to complete

### Step 3: Get API Key
1. Visit [Spoonacular API](https://spoonacular.com/food-api)
2. Sign up for **free account**
3. Copy your API key

### Step 4: Add API Key
- Open `app/src/main/java/com/example/recipesearchapp/MainActivity.kt`
- Find line: `val apiKey = "YOUR_API_KEY_HERE"`
- Replace with: `val apiKey = "your_actual_api_key"`

### Step 5: Run the App
1. Connect Android device OR start emulator
   - For emulator: Tools → Device Manager → Create Virtual Device
2. Click **Run** button (green play icon) 
3. Select your device/emulator
4. App will show random recipe titles!

## ⚠️ Prerequisites
- **Minimum Android SDK**: 29 (Android 10)
- **Target Android SDK**: 36
- **Internet connection** required for API calls

## 📱 Features

- **Popular Recipes**: Fetch and display random/popular recipes on home screen
- **Recipe Search**: Search for recipes by name, ingredients, or cuisine
- **Recipe Details**: Complete recipe information with ingredients and step-by-step instructions
- **Favorites System**: Mark recipes as favorites and store them locally
- **Offline Access**: Cached favorite recipes available without internet
- **Modern UI**: Built with Jetpack Compose following Material Design

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Dagger/Hilt
- **Networking**: Retrofit + OkHttp
- **Local Database**: Room
- **Async Programming**: Coroutines + Flow
- **API**: Spoonacular Food API

## 🏗️ Architecture Overview
```
UI Layer (Compose) → ViewModel → Repository → [Network Module | Room Database]
                                      ↓              ↓
                              Spoonacular API    Local Favorites
```

## 📂 Project Structure
```
app/src/main/java/com/example/recipesearchapp/
├── di/NetworkModule.kt           # Dependency injection setup
├── model/Recipe.kt               # Data models
├── network/RecipeApiService.kt   # API interface
├── repository/RecipeRepository.kt # Data layer
├── viewmodel/HomeViewModel.kt    # Business logic
└── MainActivity.kt               # UI layer
```

## 🔧 Troubleshooting

**Gradle sync fails?**
- File → Invalidate Caches → Restart

**App crashes?**
- Check if API key is added correctly
- Ensure internet connection

**Build errors?**
- Make sure Android SDK 29+ is installed
- Update Gradle if prompted
- Check internet connection for dependencies

**"Unresolved reference" errors?**
- File → Sync Project with Gradle Files
- Clean Project → Rebuild Project

**API not working?**
- Verify API key is correct (no extra spaces)
- Check Spoonacular API quota (free tier: 150 calls/day)

## 🔗 References

- **API Documentation**: [Spoonacular Food API Docs](https://spoonacular.com/food-api/docs)
- **Design Reference**: [Figma Design](https://www.figma.com/design/MpLnfA9X2cP2C9SBb1LDAd/Android-Onboarding-Assignment?node-id=0-1&p=f)
- **Android Architecture**: [MVVM with Compose](https://developer.android.com/topic/architecture)

## 👨‍💻 Author
**Marni Sai Sanjay** - [@MarniSaiSanjay](https://github.com/MarniSaiSanjay)

---
⭐ **Star this repo if it helped you learn Android development!**
