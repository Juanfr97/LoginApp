package com.example.loginapp.presentation.events

sealed class LoginEvent {
    data class Login(val email: String, val password: String) : LoginEvent()
    object Logout : LoginEvent()
}