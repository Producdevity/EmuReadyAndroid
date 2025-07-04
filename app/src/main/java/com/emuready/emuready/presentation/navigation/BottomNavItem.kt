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
        name = "Browse",
        route = Screen.Browse.route,
        icon = Icons.Default.Search
    ),
    BottomNavItem(
        name = "Create",
        route = Screen.Create.route,
        icon = Icons.Default.Add
    ),
    // TEMPORARY: Replaced Notifications with Emulator tab for testing Intent launching
    BottomNavItem(
        name = "Emulator",
        route = Screen.EmulatorTest.route,
        icon = Icons.Default.Settings
    ),
    BottomNavItem(
        name = "Profile",
        route = Screen.Profile.route,
        icon = Icons.Default.Person
    )
)