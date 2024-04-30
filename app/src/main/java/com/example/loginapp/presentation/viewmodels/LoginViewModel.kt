package com.example.loginapp.presentation.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginapp.data.dao.UserDao
import com.example.loginapp.domain.use_cases.IsUserLogged
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
    private val sharedPreferences: SharedPreferences,
    private val isUserLogged : IsUserLogged
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val _uiChannel = Channel<String>()
    val uiChannel = _uiChannel.receiveAsFlow()

    init {
        checkLogin()
    }

    fun onEvent(event : LoginEvent){
        when(event){
            is LoginEvent.OnLogin -> {
                viewModelScope.launch {
                    // Estado inicial de la aplicacion
                    _loginState.value = LoginState(isLoading = true)
                    // Consultar si el usuario esta logueado
                    if(isUserLogged(event.email,event.password)){
                        Log.i("LoginViewModel","El usuario se logueo correctamente")
                        _loginState.value = LoginState(isLoading = false, isLogged = true)
                        val editor = sharedPreferences.edit()
                        editor.putBoolean("isLogged",true)
                        editor.apply()
                    }
                    else {
                        _loginState.value = LoginState(isLoading = false,isLogged = false)
                        _uiChannel.send("Usuario o contraseña son incorrectos")
                    }
                }
            }
            is LoginEvent.OnLogout -> {
                val editor = sharedPreferences.edit()
                editor.putBoolean("isLogged",false)
                editor.apply()
            }
        }
    }

    fun checkLogin(){
        val isUserLogged = sharedPreferences.getBoolean("isLogged",false)
        if(isUserLogged){
            viewModelScope.launch {
                _loginState.value = LoginState(isLoading = false, isLogged = true)
            }
        }
    }
}