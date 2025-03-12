package com.example.fragranceapp.presentation.screens.collection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fragranceapp.domain.model.CollectionType
import com.example.fragranceapp.domain.model.UserCollection
import com.example.fragranceapp.presentation.common.components.ErrorMessage
import com.example.fragranceapp.presentation.common.components.FragranceCard
import com.example.fragranceapp.presentation.common.components.LoadingIndicator
import com.example.fragranceapp.presentation.viewmodel.CollectionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionScreen(
    onFragranceClick: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: CollectionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Моя коллекция") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Фильтры коллекций
            CollectionTypeFilters(
                collectionTypes = state.collectionTypes,
                selectedCollectionType = state.selectedCollectionType,
                onCollectionTypeSelect = { collectionType ->
                    viewModel.selectCollectionType(collectionType)
                }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    state.isLoading -> {
                        LoadingIndicator()
                    }
                    state.error != null -> {
                        ErrorMessage(
                            message = state.error ?: "Произошла ошибка при загрузке коллекций",
                            onRetry = { viewModel.loadUserCollections() }
                        )
                    }
                    state.collections.isEmpty() -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = "В этой коллекции пока нет ароматов",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    else -> {
                        CollectionList(
                            collections = state.collections,
                            onFragranceClick = onFragranceClick,
                            onRemoveFromCollection = { collectionId ->
                                viewModel.removeFromCollection(collectionId)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionTypeFilters(
    collectionTypes: List<CollectionType>,
    selectedCollectionType: CollectionType?,
    onCollectionTypeSelect: (CollectionType) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(collectionTypes) { collectionType ->
            FilterChip(
                selected = collectionType.id == selectedCollectionType?.id,
                onClick = { onCollectionTypeSelect(collectionType) },
                label = { Text(collectionType.name) },
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@Composable
fun CollectionList(
    collections: List<UserCollection>,
    onFragranceClick: (Int) -> Unit,
    onRemoveFromCollection: (Int) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp)
    ) {
        items(collections) { collection ->
            CollectionItem(
                collection = collection,
                onFragranceClick = onFragranceClick,
                onRemoveFromCollection = onRemoveFromCollection
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CollectionItem(
    collection: UserCollection,
    onFragranceClick: (Int) -> Unit,
    onRemoveFromCollection: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            FragranceCard(
                fragrance = collection.fragrance,
                onClick = onFragranceClick
            )
            
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Добавлено в: ${collection.collectionTypeName}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        Text(
                            text = "Дата: ${collection.addedAt}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    IconButton(
                        onClick = { onRemoveFromCollection(collection.id) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Удалить из коллекции"
                        )
                    }
                }
                
                if (!collection.notes.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Заметки: ${collection.notes}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}