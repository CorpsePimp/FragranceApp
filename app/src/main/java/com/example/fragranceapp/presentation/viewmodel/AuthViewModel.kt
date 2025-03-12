package com.example.fragranceapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragranceapp.domain.repository.AuthRepository
import com.example.fragranceapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Initial)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Initial)
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Имя пользователя и пароль не могут быть пустыми")
            return
        }

        _loginState.value = LoginState.Loading

        viewModelScope.launch {
            val result = authRepository.login(username, password)
            _loginState.value = when (result) {
                is Resource.Success -> LoginState.Success
                is Resource.Error -> LoginState.Error(result.message ?: "Неизвестная ошибка")
                is Resource.Loading -> LoginState.Loading
            }
        }
    }

    fun register(
        username: String,
        email: String,
        password: String,
        confirmPassword: String,
        firstName: String? = null,
        lastName: String? = null
    ) {
        // Проверка ввода
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            _registerState.value = RegisterState.Error("Все поля должны быть заполнены")
            return
        }

        if (!email.contains('@')) {
            _registerState.value = RegisterState.Error("Указан некорректный email")
            return
        }

        if (password != confirmPassword) {
            _registerState.value = RegisterState.Error("Пароли не совпадают")
            return
        }

        if (password.length < 8) {
            _registerState.value = RegisterState.Error("Пароль должен содержать не менее 8 символов")
            return
        }

        _registerState.value = RegisterState.Loading

        viewModelScope.launch {
            val result = authRepository.register(
                username = username,
                email = email,
                password = password,
                firstName = firstName,
                lastName = lastName
            )

            _registerState.value = when (result) {
                is Resource.Success -> RegisterState.Success
                is Resource.Error -> RegisterState.Error(result.message ?: "Неизвестная ошибка")
                is Resource.Loading -> RegisterState.Loading
            }
        }
    }

    fun resetLoginState() {
        _loginState.value = LoginState.Initial
    }

    fun resetRegisterState() {
        _registerState.value = RegisterState.Initial
    }
}

sealed class LoginState {
    object Initial : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

sealed class RegisterState {
    object Initial : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}