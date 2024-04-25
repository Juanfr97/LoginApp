package com.example.loginapp.domain.repositories

import com.example.loginapp.domain.models.User

interface UserRepository {
    suspend fun GetUserByEmailAndPassword(email:String,password:String) : User?
    suspend fun getUsers() : List<User>
}