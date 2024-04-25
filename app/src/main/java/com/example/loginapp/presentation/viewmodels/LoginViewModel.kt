package com.example.loginapp.presentation.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginapp.domain.use_cases.GetUsers
import com.example.loginapp.domain.use_cases.Login
import com.example.loginapp.presentation.events.LoginEvent
import com.example.loginapp.presentation.states.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase : Login,
    private val getUsers: GetUsers,
    private val sharedPreferences : SharedPreferences
): ViewModel(){

    private val _uiState = Channel<String>()
    val uiState = _uiState.receiveAsFlow()

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    init {
        getUsersJob()
        checkLogin()
    }
    fun onEvent(event: LoginEvent){
        when(event){
            is LoginEvent.Login -> {
                viewModelScope.launch {
                    _loginState.value = LoginState(isLoading = true)
                    val isUserlogged= loginUseCase(event.email, event.password)
                    if(!isUserlogged){
                        _uiState.send("Usuario o contraseña incorrectos")
                        _loginState.value = LoginState(error = "Usuario o contraseña incorrectos")
                    }
                    else {
                        _uiState.send("Usuario logeado")
                        _loginState.value = LoginState(isLogged = true, isLoading = false)
                        val editor = sharedPreferences.edit()
                        editor.putBoolean("isLogged",true)
                        editor.apply()
                    }
                }
            }

            LoginEvent.Logout -> {
                viewModelScope.launch {
                    _loginState.value = LoginState(isLogged = false)
                    logout()
                }
            }
        }
    }

    fun logout(){
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLogged",false)
        editor.apply()
    }

    private fun checkLogin(){
        val shared = sharedPreferences.getBoolean("isLogged",false)
        if(shared){
            viewModelScope.launch {
                _uiState.send("Usuario logeado")
                _loginState.value = LoginState(isLogged = true, isLoading = false)
            }
        }
    }

    private fun getUsersJob(){
        viewModelScope.launch {
            val users = getUsers()
            Log.i("Users",users.toString())
        }
    }

}