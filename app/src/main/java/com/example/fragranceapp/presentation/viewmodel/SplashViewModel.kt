package com.example.fragranceapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragranceapp.data.local.preferences.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn = _isUserLoggedIn.asStateFlow()

    init {
        checkUserAuthState()
    }

    private fun checkUserAuthState() {
        viewModelScope.launch {
            delay(1500) // Короткая задержка для отображения сплеш-скрина
            
            val token = tokenManager.getAccessToken()
            _isUserLoggedIn.value = token != null
            
            _isLoading.value = false
        }
    }
}