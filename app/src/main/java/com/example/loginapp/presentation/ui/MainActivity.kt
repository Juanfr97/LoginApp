package com.example.loginapp.presentation.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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
import kotlinx.coroutines.flow.collect
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
        val sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE)
        val isLogged = sharedPreferences.getBoolean("isLogged",false)
        if(isLogged){
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        loginBtn = findViewById(R.id.loginBtn)
        emailET = findViewById(R.id.emailET)
        passwordET = findViewById(R.id.passwordET)
        observeUiState()
        observeLoginState()
        loginBtn.setOnClickListener {
//            Log.i("INGENIERIASOFT","Boton presionado")
//            val email = emailET.text.toString()
//            val password = passwordET.text.toString()
//            if(email.isEmpty() || password.isEmpty()){
//                Log.i("Invalid","Invalid data")
//                Snackbar.make(it,"El correo o contraseña estan vacions",Snackbar.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            val isValidUser = MockData.users.any{ u-> u.email == email && u.password == password}
//            if(!isValidUser){
//                Snackbar.make(it,"El correo o la contraseña no son validos",Snackbar.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            val editor = sharedPreferences.edit()
//            editor.putBoolean("isLogged",true)
//            editor.apply()
//            val intent = Intent(this@MainActivity, HomeActivity::class.java)
//            startActivity(intent)
//            finish()
             login(emailET.text.toString(),passwordET.text.toString())
        }
    }

    private fun login(email:String,password:String){
        loginViewModel.onEvent(LoginEvent.Login(email,password))
    }
    private fun observeUiState(){

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                loginViewModel.uiState.collect{message ->
                    Log.i("INGENIERIASOFT",message)
                    Toast.makeText(this@MainActivity,message,Toast.LENGTH_SHORT).show()
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