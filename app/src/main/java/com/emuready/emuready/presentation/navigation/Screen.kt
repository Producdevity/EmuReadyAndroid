package com.emuready.emuready.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Browse : Screen("browse")
    object Games : Screen("games")
    object Listings : Screen("listings")
    object Create : Screen("create")
    object Notifications : Screen("notifications")
    object Profile : Screen("profile")
    object GameDetail : Screen("game/{gameId}") {
        fun createRoute(gameId: String) = "game/$gameId"
    }
    object ListingDetail : Screen("listing/{listingId}") {
        fun createRoute(listingId: String) = "listing/$listingId"
    }
    object DeviceSettings : Screen("devices")
    object EmulatorTest : Screen("emulator-test")
    object Settings : Screen("settings")
    object Login : Screen("login")
    object Register : Screen("register")
}