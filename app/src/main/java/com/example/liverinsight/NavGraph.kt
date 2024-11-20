package com.example.liverinsight

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.NavGraph
import androidx.navigation.compose.NavHost
import com.example.liverinsight.features.Screen
import com.example.liverinsight.features.auth.data.AuthViewModel
import com.example.liverinsight.features.auth.presentation.SignInScreen
import com.example.liverinsight.features.auth.presentation.SplashScreen
import com.example.liverinsight.features.auth.presentation.SignUpScreen
import com.example.liverinsight.features.home.presentation.HomeScreen
import com.example.liverinsight.features.profile.presentation.ProfileScreen


@Composable
fun NavGraph(navController: NavHostController, userViewModel: AuthViewModel = viewModel()) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(Screen.SignIn.route) {
            SignInScreen(navController = navController)
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(navController = navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
    }
}


// Define screen in app
sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object SignIn : Screen("sign_in")
    data object SignUp : Screen("sign_up")
    data object Home : Screen("home")
    data object Profile : Screen("profile")
}