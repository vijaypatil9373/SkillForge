package com.example.skillforge.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.skillforge.ui.detail.DetailScreen
import com.example.skillforge.ui.home.HomeScreen
import com.example.skillforge.ui.player.PlayerScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavRoutes.HOME) {
        composable(NavRoutes.HOME) {
            HomeScreen(navController)
        }

        composable(NavRoutes.DETAIL) { backStackEntry ->
            DetailScreen(
                navController = navController,
                categoryIndex = backStackEntry.arguments?.getString("categoryIndex")?.toIntOrNull() ?: 0,
                courseIndex = backStackEntry.arguments?.getString("courseIndex")?.toIntOrNull() ?: 0
            )
        }

        composable(NavRoutes.PLAYER) { backStackEntry ->
            PlayerScreen(
                navController = navController,
                categoryIndex = backStackEntry.arguments?.getString("categoryIndex")?.toIntOrNull() ?: 0,
                courseIndex = backStackEntry.arguments?.getString("courseIndex")?.toIntOrNull() ?: 0,
                lessonIndex = backStackEntry.arguments?.getString("lessonIndex")?.toIntOrNull() ?: 0
            )
        }
    }
}