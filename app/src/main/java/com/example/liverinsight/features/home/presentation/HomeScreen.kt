package com.example.liverinsight.features.home.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.liverinsight.features.home.data.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val state = viewModel.uiState.collectAsState()

    // Modifier scrollable added
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Liver Disease Prediction",
            style = MaterialTheme.typography.displaySmall,
            color = Color(0xffcc2b31)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input fields for parameters
        InputField(label = "Age", value = state.value.age, onValueChange = viewModel::onAgeChange)

        Spacer(modifier = Modifier.height(16.dp))

        // Gender input using RadioButton
        GenderInput(
            selectedGender = state.value.gender,
            onGenderChange = { viewModel.onGenderChange(it) }
        )

        InputField(
            label = "Total Bilirubin",
            value = state.value.totalBilirubin,
            onValueChange = viewModel::onTotalBilirubinChange
        )
        InputField(
            label = "Direct Bilirubin",
            value = state.value.directBilirubin,
            onValueChange = viewModel::onDirectBilirubinChange
        )
        InputField(
            label = "Alkaline Phosphotase",
            value = state.value.alkalinePhosphotase,
            onValueChange = viewModel::onAlkalinePhosphotaseChange
        )
        InputField(label = "SGPT", value = state.value.sgpt, onValueChange = viewModel::onSgptChange)
        InputField(label = "SGOT", value = state.value.sgot, onValueChange = viewModel::onSgotChange)
        InputField(
            label = "Total Protein",
            value = state.value.totalProtein,
            onValueChange = viewModel::onTotalProteinChange
        )
        InputField(label = "Albumin", value = state.value.albumin, onValueChange = viewModel::onAlbuminChange)
        InputField(
            label = "Albumin and Globulin Ratio",
            value = state.value.albuminGlobulinRatio,
            onValueChange = viewModel::onAlbuminGlobulinRatioChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display model loading status
        Text(
            text = "Model Status: ${state.value.modelStatus}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to predict
        Button(
            onClick = { viewModel.predictDisease() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffcc2b31))
        ) {
            Text("Predict")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display prediction result or error message
        if (state.value.errorMessage != null) {
            Text(
                text = state.value.errorMessage ?: "Unknown error",  // safe call dengan fallback default
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Red
            )
        } else {
            Text(
                text = state.value.predictionResult ?: "No prediction yet",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xffcc2b31)
            )
        }
    }
}

@Composable
fun InputField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xffcc2b31),
            cursorColor = Color(0xffcc2b31),
            focusedLabelColor = Color(0xffcc2b31)
        )
    )
}

@Composable
fun GenderInput(selectedGender: String, onGenderChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Gender", style = MaterialTheme.typography.bodyLarge)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Radio button for Female
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedGender == "0",
                    onClick = { onGenderChange("0") },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = if (selectedGender == "0") Color(0xffcc2b31) else Color.Gray,
                        unselectedColor = Color.Gray
                    )
                )
                Text("Female")
            }
            // Radio button for Male
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedGender == "1",
                    onClick = { onGenderChange("1") },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = if (selectedGender == "1") Color(0xffcc2b31) else Color.Gray,
                        unselectedColor = Color.Gray
                    )
                )
                Text("Male")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    val mockNavController = rememberNavController()
    HomeScreen(navController = mockNavController)
}

