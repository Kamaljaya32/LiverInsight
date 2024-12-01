package com.example.liverinsight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.example.liverinsight.features.home.data.DownloadModelComposable
import com.example.liverinsight.ui.theme.LiverInsightTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LiverInsightTheme(darkTheme = false) {
                val navController = rememberNavController()
                Surface(color = MaterialTheme.colorScheme.background) {
                    DownloadModelComposable()
                    NavGraph(navController = navController)
                }
            }
        }
    }
}

