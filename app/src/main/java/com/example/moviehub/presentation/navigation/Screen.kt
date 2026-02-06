package com.example.moviehub.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object Details : Screen("details_screen/{movieId}") {
        fun passId(id: Int) = "details_screen/$id"
    }
    object Search : Screen("search_screen")
    object Favorites : Screen("favorites_screen")
    object Auth : Screen("auth_screen")
}