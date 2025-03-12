package com.example.fragranceapp.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Login : Screen("login_screen")
    object Register : Screen("register_screen")
    object Catalog : Screen("catalog_screen")
    object FragranceDetail : Screen("fragrance_detail_screen/{fragranceId}") {
        fun createRoute(fragranceId: Int) = "fragrance_detail_screen/$fragranceId"
    }
    object Collection : Screen("collection_screen")
    object Profile : Screen("profile_screen")
}