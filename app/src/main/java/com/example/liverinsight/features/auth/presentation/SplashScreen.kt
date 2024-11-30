package com.example.liverinsight.features.auth.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.liverinsight.R
import com.example.liverinsight.Screen
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavController){
    LaunchedEffect(true) {
        delay(2000)
        navController.navigate(Screen.SignIn.route){
            popUpTo(Screen.Splash.route){
                inclusive = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val image: Painter = painterResource(id = R.drawable.project1)
            Image(
                painter = image,
                contentDescription = "Liver Insight Logo",
                modifier = Modifier
                    .size(300.dp)
                    .offset(y = -50.dp)
            )
            Spacer(modifier = Modifier.height(1.dp)) // Jarak antara gambar dan teks
            Text(
                text = "Future of Wellness",
                color = Color(0xffcc2b31),
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 1.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(navController = rememberNavController())
}