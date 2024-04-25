package com.example.loginapp.domain.use_cases

import com.example.loginapp.domain.models.User
import com.example.loginapp.domain.repositories.UserRepository

class GetUsers(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke() : List<User> {
        return userRepository.getUsers()
    }
}