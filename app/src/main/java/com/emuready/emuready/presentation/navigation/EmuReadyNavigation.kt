package com.emuready.emuready.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.emuready.emuready.presentation.ui.screens.*

@Composable
fun EmuReadyNavigation(
    navController: NavHostController,
    startDestination: String = Screen.Home.route,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
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
                },
                onNavigateToBrowse = {
                    navController.navigate(Screen.Browse.route)
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
        
        composable(Screen.Games.route) {
            BrowseScreen(
                onNavigateToGame = { gameId ->
                    navController.navigate(Screen.GameDetail.createRoute(gameId))
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Listings.route) {
            ListingsScreen(
                onNavigateToListing = { listingId ->
                    navController.navigate(Screen.ListingDetail.createRoute(listingId))
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToCreate = {
                    navController.navigate(Screen.Create.route)
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
                },
                onNavigateToSignIn = {
                    navController.navigate(Screen.Login.route)
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
            SignInScreen(
                onNavigateToSignUp = {
                    navController.navigate(Screen.Register.route)
                },
                onSignInSuccess = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Register.route) {
            SignUpScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSignUpSuccess = {
                    navController.popBackStack()
                }
            )
        }
    }
}