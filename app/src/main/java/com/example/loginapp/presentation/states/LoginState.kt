package com.example.loginapp.presentation.states

data class LoginState(
    val isLoading: Boolean = false,
    val isLogged: Boolean = false,
    val email: String = "",
    val password: String = "",
    val error: String = ""
)
