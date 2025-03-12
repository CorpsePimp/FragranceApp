package com.example.fragranceapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fragranceapp.data.local.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userPreferences: UserPreferences
) : ViewModel() {
    
    val isDarkMode = userPreferences.isDarkMode
}