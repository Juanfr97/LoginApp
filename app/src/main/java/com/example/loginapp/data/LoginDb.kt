package com.example.loginapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.loginapp.data.dao.UserDao
import com.example.loginapp.domain.models.User

@Database(entities = [User::class], version = 1)
abstract class LoginDb : RoomDatabase() {
    abstract fun userDao() : UserDao
}