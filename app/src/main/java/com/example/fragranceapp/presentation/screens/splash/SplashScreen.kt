package com.example.fragranceapp.presentation.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fragranceapp.presentation.viewmodel.SplashViewModel

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToCatalog: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsState()

    LaunchedEffect(key1 = isLoading) {
        if (!isLoading) {
            if (isUserLoggedIn) {
                onNavigateToCatalog()
            } else {
                onNavigateToLogin()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        // Замените R.drawable.app_logo на ваш настоящий логотип
        // Или используйте текст, если логотип отсутствует
        // Image(
        //     painter = painterResource(id = R.drawable.app_logo),
        //     contentDescription = "App Logo",
        //     modifier = Modifier.size(180.dp)
        // )
        
        // Временная замена логотипа на текст
        Text(
            text = "Fragrance\nApp",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
    }
}