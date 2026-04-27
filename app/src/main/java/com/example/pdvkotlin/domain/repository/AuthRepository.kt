package com.example.pdvkotlin.domain.repository

import com.example.pdvkotlin.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<User?>
    suspend fun login(username: String, password: String): Result<User>
    suspend fun logout()
}
