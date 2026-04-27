package com.example.pdvkotlin.domain.usecase

import com.example.pdvkotlin.domain.model.User
import com.example.pdvkotlin.domain.model.UserRole
import com.example.pdvkotlin.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class LoginUseCaseTest {
    private val repository = mockk<AuthRepository> {
        coEvery { login("admin", "admin123") } returns Result.success(
            User("u1", "Administrador", "admin", UserRole.ADMIN)
        )
        coEvery { currentUser } returns emptyFlow()
    }

    @Test
    fun trimsUsernameBeforeLogin() = runTest {
        val result = LoginUseCase(repository)(" admin ", "admin123")

        assertEquals("admin", result.getOrThrow().username)
    }
}
