package com.example.hidrett_app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hidrett_app.ui.screens.RecoverAccountScreen
import com.example.hidrett_app.ui.screens.LoginScreen
import com.example.hidrett_app.ui.screens.MainScreen
import com.example.hidrett_app.ui.screens.Routes
import com.example.hidrett_app.ui.screens.WelcomeScreen

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("Login") {
            LoginScreen(navController = navController)
        }

        composable("Welcome") {
            WelcomeScreen(navController=navController)
        }

        composable(Routes.FORGOT_PASSWORD) {
            RecoverAccountScreen(navController = navController)
        }

        composable("MainScreen") {
            MainScreen(navController = navController)
        }
    }

}