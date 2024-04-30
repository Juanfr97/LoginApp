package com.example.loginapp.presentation.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.loginapp.R
import com.example.loginapp.data.mockdata.MockData
import com.example.loginapp.presentation.events.LoginEvent
import com.example.loginapp.presentation.viewmodels.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var loginBtn : Button
    private lateinit var emailET : EditText
    private lateinit var passwordET : EditText
    private val loginViewModel by viewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loginBtn = findViewById(R.id.loginBtn)
        emailET = findViewById(R.id.emailET)
        passwordET = findViewById(R.id.passwordET)
        loginBtn.setOnClickListener {
            val email = emailET.text.toString()
            val password = passwordET.text.toString()

            if(email.isEmpty() || password.isEmpty()){
                Snackbar.make(loginBtn,"Por favor llena todos los campos",Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            login(email,password)
        }
        observeLoginState()
        obserUIState()
    }

    private fun login(email:String,password:String){
        loginViewModel.onEvent(LoginEvent.OnLogin(email,password))
    }

    private fun obserUIState(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                loginViewModel.uiChannel.collect { message ->
                    Snackbar.make(loginBtn,message,Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeLoginState(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                loginViewModel.loginState.collect{loginState ->
                    if(loginState.isLogged){
                        Log.i("INGENIERIASOFT","Usuario logeado")
                        val intent = Intent(this@MainActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}