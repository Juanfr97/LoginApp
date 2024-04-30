package com.example.loginapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.loginapp.domain.models.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    suspend fun getUsers() : List<User>

    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    suspend fun getUserByEmailAndPassword(email : String,password:String) : User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(user:List<User>)
}