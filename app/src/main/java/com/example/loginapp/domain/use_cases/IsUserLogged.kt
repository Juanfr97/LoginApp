package com.example.loginapp.domain.use_cases

import com.example.loginapp.data.dao.UserDao
import javax.inject.Inject

class IsUserLogged @Inject constructor(
    private val userDao: UserDao
) {
    suspend operator fun invoke(email:String,password:String):Boolean{
        val user = userDao.getUserByEmailAndPassword(email,password)
        return user != null
    }
}