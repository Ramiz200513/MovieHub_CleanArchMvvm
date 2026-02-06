package com.example.moviehub.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.moviehub.presentation.screens.auth.AuthScreen
import com.example.moviehub.presentation.screens.detail.MovieDetailsScreen
import com.example.moviehub.presentation.screens.favorite.FavoritesScreen
import com.example.moviehub.presentation.screens.home.HomeScreen
import com.example.moviehub.presentation.screens.search.SearchScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController,
        startDestination = Screen.Auth.route
    ){
        composable(
            route = Screen.Home.route,
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(300))
            }
        ){
            HomeScreen(navController = navController)
        }
        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument("movieId") { type = NavType.IntType }),
            enterTransition = {

                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300))
            },
            popExitTransition = {

                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300))
            }
        ) {
            MovieDetailsScreen(navController = navController)
        }
        composable(
            route = Screen.Search.route,
            enterTransition = { slideInVertically(initialOffsetY = { it }) },
            popExitTransition = { slideOutVertically(targetOffsetY = { it }) }
        ) {
            SearchScreen(navController = navController)
        }
        composable(route = Screen.Favorites.route) {
            FavoritesScreen(navController = navController)
        }
        composable(route = Screen.Auth.route) {
            AuthScreen(navController = navController)
        }
    }
}
