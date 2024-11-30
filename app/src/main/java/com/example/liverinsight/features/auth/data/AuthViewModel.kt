package com.example.liverinsight.features.auth.data

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    var authResult by mutableStateOf<Result<Unit>?>(null)
        private set

    var currentUser by mutableStateOf(auth.currentUser)

    var name by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var email by mutableStateOf("")

    fun signUp(email: String, password: String) {
        if (!isValidEmail(email) || !isValidPassword(password)) {
            authResult = Result.failure(IllegalArgumentException("Invalid email or password"))
            return
        }
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                authResult = Result.success(Unit)
                currentUser = auth.currentUser
            } catch (e: Exception) {
                authResult = Result.failure(e)
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                authResult = Result.success(Unit)
                currentUser = auth.currentUser
            } catch (e: Exception) {
                authResult = Result.failure(e)
            }
        }
    }

    fun signOut() {
        auth.signOut()
        currentUser = null
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
}

