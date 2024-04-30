package com.example.loginapp.data.mockdata

import com.example.loginapp.domain.models.User

class MockData{
    companion object{
        val users = listOf<User>(
            User(1,"juanfr97@hotmail.com","123456"),
            User(2,"juanfr98@hotmail.com","123456"),
            User(3,"juanfr99@hotmail.com","123456")
        )

        fun getUsers(){
            println("Get users")
        }
    }
}