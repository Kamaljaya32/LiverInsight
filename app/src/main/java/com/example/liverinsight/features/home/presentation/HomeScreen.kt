package com.example.liverinsight.features.home.presentation

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.liverinsight.features.auth.presentation.SignUpScreen
import android.widget.CheckBox
import androidx.compose.foundation.layout.Column

@Composable
fun HomeScreen(navController: NavController) {
    val ScrollState = rememberScrollState()


}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen(){
    val mockNavController = rememberNavController()
    HomeScreen(navController = mockNavController)
}
