package com.example.pdvkotlin.domain.usecase

import com.example.pdvkotlin.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String) =
        repository.login(username.trim(), password)
}

class ObserveSessionUseCase @Inject constructor(repository: AuthRepository) {
    val currentUser = repository.currentUser
}

class LogoutUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.logout()
}
