package com.example.fragranceapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.fragranceapp.presentation.screens.auth.LoginScreen
import com.example.fragranceapp.presentation.screens.auth.RegisterScreen
import com.example.fragranceapp.presentation.screens.catalog.CatalogScreen
import com.example.fragranceapp.presentation.screens.collection.CollectionScreen
import com.example.fragranceapp.presentation.screens.details.FragranceDetailScreen
import com.example.fragranceapp.presentation.screens.profile.ProfileScreen
import com.example.fragranceapp.presentation.screens.splash.SplashScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToCatalog = {
                    navController.navigate(Screen.Catalog.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Catalog.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(route = Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable(route = Screen.Catalog.route) {
            CatalogScreen(
                onFragranceClick = { fragranceId ->
                    navController.navigate(Screen.FragranceDetail.createRoute(fragranceId))
                },
                onNavigateToCollection = {
                    navController.navigate(Screen.Collection.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        composable(
            route = Screen.FragranceDetail.route,
            arguments = listOf(
                navArgument("fragranceId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val fragranceId = backStackEntry.arguments?.getInt("fragranceId") ?: -1
            FragranceDetailScreen(
                fragranceId = fragranceId,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToCollection = {
                    navController.navigate(Screen.Collection.route)
                }
            )
        }

        composable(route = Screen.Collection.route) {
            CollectionScreen(
                onFragranceClick = { fragranceId ->
                    navController.navigate(Screen.FragranceDetail.createRoute(fragranceId))
                },
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable(route = Screen.Profile.route) {
            ProfileScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Catalog.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}