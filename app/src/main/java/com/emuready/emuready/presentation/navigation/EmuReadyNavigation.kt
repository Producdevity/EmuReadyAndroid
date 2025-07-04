package com.emuready.emuready.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.emuready.emuready.presentation.ui.screens.*

@Composable
fun EmuReadyNavigation(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToGame = { gameId ->
                    navController.navigate(Screen.GameDetail.createRoute(gameId))
                },
                onNavigateToCreate = {
                    navController.navigate(Screen.Create.route)
                },
                onNavigateToEmulatorTest = {
                    navController.navigate(Screen.EmulatorTest.route)
                }
            )
        }
        
        composable(Screen.Browse.route) {
            BrowseScreen(
                onNavigateToGame = { gameId ->
                    navController.navigate(Screen.GameDetail.createRoute(gameId))
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Create.route) {
            CreateListingScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onListingCreated = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Notifications.route) {
            NotificationsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToDevices = {
                    navController.navigate(Screen.DeviceSettings.route)
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }
        
        composable(
            route = Screen.GameDetail.route,
            arguments = listOf(navArgument("gameId") { type = NavType.StringType })
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId") ?: ""
            GameDetailScreen(
                gameId = gameId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToListing = { listingId ->
                    navController.navigate(Screen.ListingDetail.createRoute(listingId))
                },
                onNavigateToCreate = {
                    navController.navigate(Screen.Create.route)
                }
            )
        }
        
        composable(
            route = Screen.ListingDetail.route,
            arguments = listOf(navArgument("listingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val listingId = backStackEntry.arguments?.getString("listingId") ?: ""
            ListingDetailScreen(
                listingId = listingId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.DeviceSettings.route) {
            DeviceManagementScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.EmulatorTest.route) {
            EmulatorTestScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onLoginSuccess = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.popBackStack()
                }
            )
        }
    }
}