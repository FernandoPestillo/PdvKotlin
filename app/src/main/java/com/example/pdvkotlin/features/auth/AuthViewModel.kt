package com.example.pdvkotlin.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pdvkotlin.domain.model.User
import com.example.pdvkotlin.domain.usecase.LoginUseCase
import com.example.pdvkotlin.domain.usecase.ObserveSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "admin",
    val password: String = "admin123",
    val loading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    observeSessionUseCase: ObserveSessionUseCase,
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    val user: StateFlow<User?> = observeSessionUseCase.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _loginState = MutableStateFlow(LoginUiState())
    val loginState: StateFlow<LoginUiState> = _loginState

    fun setUsername(value: String) = _loginState.update { it.copy(username = value, error = null) }
    fun setPassword(value: String) = _loginState.update { it.copy(password = value, error = null) }

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loginState.update { it.copy(loading = true, error = null) }
            loginUseCase(loginState.value.username, loginState.value.password)
                .onSuccess { onSuccess() }
                .onFailure { error -> _loginState.update { it.copy(error = error.message) } }
            _loginState.update { it.copy(loading = false) }
        }
    }
}

