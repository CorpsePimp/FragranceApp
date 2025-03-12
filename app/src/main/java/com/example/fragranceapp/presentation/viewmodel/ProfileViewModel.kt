package com.example.fragranceapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragranceapp.data.local.preferences.TokenManager
import com.example.fragranceapp.data.local.preferences.UserPreferences
import com.example.fragranceapp.domain.model.User
import com.example.fragranceapp.domain.repository.AuthRepository
import com.example.fragranceapp.domain.repository.UserRepository
import com.example.fragranceapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        loadUserData()
        observeDarkModePreference()
    }

    private fun observeDarkModePreference() {
        viewModelScope.launch {
            userPreferences.isDarkMode.collectLatest { isDarkMode ->
                _state.update { it.copy(isDarkMode = isDarkMode) }
            }
        }
    }

    fun loadUserData() {
        _state.update { it.copy(isLoading = true, error = null) }
        
        viewModelScope.launch {
            val result = userRepository.getCurrentUser()
            
            when (result) {
                is Resource.Success -> {
                    _state.update { 
                        it.copy(
                            user = result.data,
                            isLoading = false
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update { 
                        it.copy(
                            error = result.message,
                            isLoading = false
                        )
                    }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            tokenManager.clearTokens()
            userPreferences.clearUserData()
        }
    }

    fun setDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            userPreferences.setDarkMode(isDarkMode)
        }
    }
}

data class ProfileState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isDarkMode: Boolean = false
)