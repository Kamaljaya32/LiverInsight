package com.example.liverinsight.features.auth.presentation

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.liverinsight.R
import com.example.liverinsight.features.auth.data.AuthViewModel
import com.example.liverinsight.composable.button.Button
import com.example.liverinsight.composable.button.OutlinedButton
import com.example.liverinsight.composable.textfield.TextField
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

@Composable
fun SignInScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    val scrollState = rememberScrollState()

    // State for email and password
    var password by remember { mutableStateOf("") }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Image(
                painter = painterResource(id = R.drawable.project1),
                contentDescription = "Liver Insight Logo",
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(32.dp))


            Text(
                "Welcome to Liver Insight",
                color = Color (0xffcc2b31),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = authViewModel.email,
                onValueChange = { authViewModel.email = it },
                placeholderText = "Your email",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                placeholderText = "Password",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                text = "Sign In",
                onClick = {
                    if (authViewModel.email.isEmpty() || password.isEmpty()) {
                        snackbarMessage = "Please fill in all fields"
                        showSnackbar = true
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(authViewModel.email).matches()) {
                        snackbarMessage = "Please enter a valid email"
                        showSnackbar = true
                    }
                    else {
                        authViewModel.signIn(authViewModel.email, password)
                    }
                }
            )

            // Observasi hasil autentikasi
            LaunchedEffect(authViewModel.authResult) {
                authViewModel.authResult?.let { result ->
                    if (result.isSuccess) {
                        // Navigasi ke halaman Home jika berhasil
                        navController.navigate("home") {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    } else {
                        // Tangkap dan tangani exception
                        when (val exception = result.exceptionOrNull()) {
                            is FirebaseAuthInvalidUserException -> {
                                snackbarMessage = "User not found. Please check your email or register."
                            }
                            is FirebaseAuthInvalidCredentialsException -> {
                                snackbarMessage = "Invalid credentials. Please check your email and password."
                            }
                            else -> {
                                snackbarMessage = exception?.message ?: "Sign In Failed. Please try again."
                            }
                        }
                        showSnackbar = true
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    color = Color.Gray
                )
                Text(
                    "or",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.Gray
                )
                HorizontalDivider(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                text = "Sign Up",
                onClick = {
                    navController.navigate("sign_up")
                }
            )
        }

        // Snackbar
        if (showSnackbar) {
            LaunchedEffect(snackbarHostState) {
                snackbarHostState.showSnackbar(
                    message = snackbarMessage,
                    duration = SnackbarDuration.Short
                )
                showSnackbar = false
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) { data ->
            Snackbar(
                modifier = Modifier.padding(horizontal = 16.dp),
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
            ) {
                Text(data.visuals.message)
            }
        }
    }
}

data class ButtonBarItem(val name: String, val icon: ImageVector, val route: String)

@Preview(showBackground = true)
@Composable
fun PreviewSignInScreen() {
    val mockNavController = rememberNavController()
    SignInScreen(navController = mockNavController)
}
