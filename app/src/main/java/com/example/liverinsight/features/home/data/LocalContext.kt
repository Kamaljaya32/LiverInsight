package com.example.liverinsight.features.home.data

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import com.google.firebase.ml.modeldownloader.DownloadType

@Composable
fun DownloadModelComposable() {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val conditions = CustomModelDownloadConditions.Builder()
            .requireWifi()
            .build()

        FirebaseModelDownloader.getInstance()
            .getModel("liver-detection2", DownloadType.LOCAL_MODEL, conditions)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Model downloaded successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Model download failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
