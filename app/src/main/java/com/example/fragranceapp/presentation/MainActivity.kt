package com.example.fragranceapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.fragranceapp.data.local.preferences.UserPreferences
import com.example.fragranceapp.presentation.common.theme.FragranceAppTheme
import com.example.fragranceapp.presentation.navigation.NavigationGraph
import com.example.fragranceapp.presentation.navigation.Screen
import com.example.fragranceapp.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FragranceAppApp()
        }
    }
}

@Composable
fun FragranceAppApp(
    viewModel: MainViewModel = hiltViewModel()
) {
    val isDarkMode by viewModel.isDarkMode.collectAsState(initial = false)
    val navController = rememberNavController()

    FragranceAppTheme(
        darkTheme = isDarkMode
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold { innerPadding ->
                NavigationGraph(
                    navController = navController,
                    startDestination = Screen.Splash.route,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}