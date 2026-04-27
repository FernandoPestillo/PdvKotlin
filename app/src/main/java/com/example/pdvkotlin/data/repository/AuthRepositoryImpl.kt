package com.example.pdvkotlin.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.pdvkotlin.domain.model.User
import com.example.pdvkotlin.domain.model.UserRole
import com.example.pdvkotlin.domain.repository.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.sessionDataStore by preferencesDataStore("session")

@Singleton
class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : AuthRepository {
    private val userIdKey = stringPreferencesKey("user_id")
    private val nameKey = stringPreferencesKey("name")
    private val usernameKey = stringPreferencesKey("username")
    private val roleKey = stringPreferencesKey("role")

    private val users = listOf(
        User("u1", "Administrador", "admin", UserRole.ADMIN) to "admin123",
        User("u2", "Caixa", "caixa", UserRole.CASHIER) to "caixa123",
        User("u3", "Gerente", "gerente", UserRole.MANAGER) to "gerente123",
    )

    override val currentUser: Flow<User?> = context.sessionDataStore.data.map { preferences ->
        val id = preferences[userIdKey] ?: return@map null
        User(
            id = id,
            name = preferences[nameKey].orEmpty(),
            username = preferences[usernameKey].orEmpty(),
            role = UserRole.valueOf(preferences[roleKey] ?: UserRole.CASHIER.name),
        )
    }

    override suspend fun login(username: String, password: String): Result<User> {
        val match = users.firstOrNull { it.first.username == username && it.second == password }
            ?: return Result.failure(IllegalArgumentException("Usuario ou senha invalidos"))

        context.sessionDataStore.edit {
            it[userIdKey] = match.first.id
            it[nameKey] = match.first.name
            it[usernameKey] = match.first.username
            it[roleKey] = match.first.role.name
        }
        return Result.success(match.first)
    }

    override suspend fun logout() {
        context.sessionDataStore.edit { it.clear() }
    }
}
