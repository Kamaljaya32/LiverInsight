package com.example.liverinsight.composable.appbar

import android.graphics.drawable.Icon
import com.example.liverinsight.Screen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun ButtonBar(
    currentScreen: String,
    navController: NavController,
    onItemSelected: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color(0xffcc2b31),
        contentColor = Color.White
    ) {
        val item = listOf(
            ButtonBarItem("Home", Icons.Default.Home, Screen.Home.route),
        )

        item.forEach { item ->
            NavigationBarItem(
                icon = { androidx.compose.material3.Icon(item.icon, contentDescription = item.name) },
                selected = currentScreen == item.route,
                onClick = {
                    // Navigasi ke item yang dipilih
                    if (navController.currentDestination?.route != item.route) {
                        navController.navigate(item.route) {
                            // Menghindari salinan rute yang sama
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                    onItemSelected(item.route)
                },
                label = {
                    Text(
                        text = item.name,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.White,
                    selectedTextColor = Color.Black,
                    unselectedTextColor = Color.White
                )
            )
        }
    }
}

data class ButtonBarItem(val name: String, val icon: ImageVector, val route: String)