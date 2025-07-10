package com.emuready.emuready.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector,
    val badgeCount: Int = 0
)

val bottomNavItems = listOf(
    BottomNavItem(
        name = "Home",
        route = Screen.Home.route,
        icon = Icons.Default.Home
    ),
    BottomNavItem(
        name = "Games",
        route = Screen.Games.route,
        icon = Icons.Default.SportsEsports
    ),
    BottomNavItem(
        name = "Listings",
        route = Screen.Listings.route,
        icon = Icons.Default.ShoppingCart
    ),
    BottomNavItem(
        name = "Profile",
        route = Screen.Profile.route,
        icon = Icons.Default.Person
    )
)