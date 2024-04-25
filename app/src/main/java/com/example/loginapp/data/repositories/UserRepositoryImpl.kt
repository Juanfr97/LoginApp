package com.example.loginapp.data.repositories

import com.example.loginapp.data.dao.UserDao
import com.example.loginapp.domain.models.User
import com.example.loginapp.domain.repositories.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {
    override suspend fun GetUserByEmailAndPassword(email: String, password: String): User? {
        try{
            val user = userDao.getUserByEmailAndPassword(email, password)
            return user
        }
        catch (e: Exception){
            throw e
        }
    }

    override suspend fun getUsers(): List<User> {
        return userDao.getUsers()
    }
}