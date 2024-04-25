package com.example.loginapp.domain.use_cases

import android.util.Log
import com.example.loginapp.domain.repositories.UserRepository

class Login(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String) : Boolean {

        return try{
            val user = userRepository.GetUserByEmailAndPassword(email, password)
            Log.i("UserFound", "User: $user")
            user != null
        } catch (e: Exception){
            Log.e("UserFound", "Error: $e")
            false
        }

    }
}