package com.example.loginapp.presentation.events

sealed class LoginEvent {
    data class OnLogin(val email : String, val password : String) : LoginEvent()
    object OnLogout : LoginEvent()
}