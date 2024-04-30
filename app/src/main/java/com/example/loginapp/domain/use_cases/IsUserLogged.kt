package com.example.loginapp.domain.use_cases

import com.example.loginapp.data.dao.UserDao

class IsUserLogged(
    private val userDao: UserDao
) {
    suspend operator fun invoke(email:String,password:String):Boolean{
        val user = userDao.getUserByEmailAndPassword(email,password)
        return user != null
    }
}