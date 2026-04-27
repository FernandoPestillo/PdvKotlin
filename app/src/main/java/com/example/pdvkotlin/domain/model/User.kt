package com.example.pdvkotlin.domain.model

enum class UserRole { ADMIN, CASHIER, MANAGER }

data class User(
    val id: String,
    val name: String,
    val username: String,
    val role: UserRole,
)
