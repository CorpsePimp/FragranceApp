package com.example.fragranceapp.presentation.screens.catalog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fragranceapp.presentation.common.components.ErrorMessage
import com.example.fragranceapp.presentation.common.components.FragranceCard
import com.example.fragranceapp.presentation.viewmodel.CatalogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    onFragranceClick: (Int) -> Unit,
    onNavigateToCollection: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: CatalogViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showFilterDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Каталог ароматов") },
                actions = [
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Профиль"
                        )
                    }
                ]
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(
                query = state.searchQuery,
                onQueryChange = viewModel::updateSearchQuery,
                onSearch = { viewModel.applyFilters() },
                active = false,
                onActiveChange = { },
                placeholder = { Text("Поиск ароматов") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Фильтры")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) { }

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    state.error != null -> {
                        ErrorMessage(
                            message = state.error ?: "Произошла ошибка при загрузке данных",
                            onRetry = { viewModel.loadFragrances() }
                        )
                    }
                    state.fragrances.isEmpty() -> {
                        Text(
                            text = "Ароматы не найдены",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    else -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(8.dp)
                        ) {
                            items(state.fragrances) { fragrance ->
                                FragranceCard(
                                    fragrance = fragrance,
                                    onClick = onFragranceClick,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
        
        if (showFilterDialog) {
            FilterDialog(
                initialBrandId = state.selectedBrandId,
                initialFamilyId = state.selectedFamilyId,
                initialConcentrationId = state.selectedConcentrationId,
                initialMinRating = state.selectedMinRating,
                initialYear = state.selectedYear,
                onDismiss = { showFilterDialog = false },
                onApplyFilters = { brandId, familyId, concentrationId, minRating, year ->
                    viewModel.updateSelectedBrandId(brandId)
                    viewModel.updateSelectedFamilyId(familyId)
                    viewModel.updateSelectedConcentrationId(concentrationId)
                    viewModel.updateSelectedMinRating(minRating)
                    viewModel.updateSelectedYear(year)
                    viewModel.applyFilters()
                    showFilterDialog = false
                }
            )
        }
    }
}