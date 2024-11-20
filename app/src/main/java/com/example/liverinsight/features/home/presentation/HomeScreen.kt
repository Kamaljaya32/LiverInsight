package com.example.liverinsight.features.home.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.liverinsight.R
import com.example.liverinsight.composable.button.Button
import com.example.liverinsight.composable.button.OutlinedButton

@Composable
fun HomeScreen(navController: NavController) {
    val gradientColors = listOf(
        Color(0xFFFCE4EC),  // Light pink
        Color(0xFFF3E5F5),  // Light purple
        Color(0xFFE8EAF6)   // Light indigo
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(colors = gradientColors)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Image(
                painter = painterResource(id = R.drawable.project1), // Replace with your image
                contentDescription = "Liver Insight Logo",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                "Welcome to Liver Insight!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.W400
            )

            Text(
                "Your journey to better health starts here.",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Button untuk pindah ke Profile atau halaman lain
            Button(
                text = "Go to Profile",
                onClick = {
                    navController.navigate("profile") // Replace with the actual route name
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Button to navigate to SignIn
            OutlinedButton(
                text = "Sign In",
                onClick = {
                    navController.navigate("sign_in")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Button to navigate to SignUp
            OutlinedButton(
                text = "Sign Up",
                onClick = {
                    navController.navigate("sign_up")
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    val mockNavController = rememberNavController()
    HomeScreen(navController = mockNavController)
}
