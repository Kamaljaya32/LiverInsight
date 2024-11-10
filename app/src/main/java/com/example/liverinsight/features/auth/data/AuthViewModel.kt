package com.example.liverinsight.features.auth.data

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    // State variables for user input
    var name by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var email by mutableStateOf("")

    // Function to update user data
    fun updateUserData(newName: String, newPhoneNumber: String, newEmail: String) {
        viewModelScope.launch {
            name = newName
            phoneNumber = newPhoneNumber
            email = newEmail
        }
    }

    // Function to clear user data
    fun clearUserData() {
        viewModelScope.launch {
            name = ""
            phoneNumber = ""
            email = ""
        }
    }
}
