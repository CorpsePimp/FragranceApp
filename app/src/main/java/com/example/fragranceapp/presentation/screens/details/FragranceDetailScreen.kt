package com.example.fragranceapp.presentation.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.fragranceapp.domain.model.FragranceDetail
import com.example.fragranceapp.domain.model.Review
import com.example.fragranceapp.presentation.common.components.ErrorMessage
import com.example.fragranceapp.presentation.common.components.LoadingIndicator
import com.example.fragranceapp.presentation.common.components.RatingBar
import com.example.fragranceapp.presentation.viewmodel.FragranceDetailViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FragranceDetailScreen(
    fragranceId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToCollection: () -> Unit,
    viewModel: FragranceDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var showAddToCollectionDialog by remember { mutableStateOf(false) }
    var showAddReviewDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.addToCollectionError, state.showAddToCollectionSuccess) {
        when {
            state.addToCollectionError != null -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        "Ошибка: ${state.addToCollectionError}"
                    )
                    viewModel.resetAddToCollectionMessages()
                }
            }
            state.showAddToCollectionSuccess -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        "Аромат добавлен в коллекцию"
                    )
                    viewModel.resetAddToCollectionMessages()
                }
            }
        }
    }

    LaunchedEffect(state.showAddReviewSuccess) {
        if (state.showAddReviewSuccess) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    "Отзыв успешно добавлен"
                )
                viewModel.resetAddReviewMessages()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(state.fragranceDetail?.name ?: "Детали аромата") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showAddToCollectionDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.BookmarkAdd,
                            contentDescription = "Добавить в коллекцию"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    LoadingIndicator()
                }
                state.error != null -> {
                    ErrorMessage(
                        message = state.error ?: "Произошла ошибка при загрузке данных",
                        onRetry = { viewModel.loadFragranceDetails() }
                    )
                }
                state.fragranceDetail != null -> {
                    FragranceDetailContent(
                        fragranceDetail = state.fragranceDetail!!,
                        selectedTabIndex = selectedTabIndex,
                        onTabSelected = { selectedTabIndex = it },
                        onAddToCollection = { showAddToCollectionDialog = true },
                        onAddReview = { showAddReviewDialog = true }
                    )
                }
            }
        }
    }

    if (showAddToCollectionDialog && state.fragranceDetail != null) {
        AddToCollectionDialog(
            collectionTypes = state.collectionTypes,
            fragranceName = state.fragranceDetail!!.name,
            isLoading = state.isAddingToCollection,
            onDismiss = { showAddToCollectionDialog = false },
            onConfirm = { collectionTypeId, notes ->
                viewModel.addToCollection(collectionTypeId, notes)
                showAddToCollectionDialog = false
            }
        )
    }

    if (showAddReviewDialog && state.fragranceDetail != null) {
        AddReviewDialog(
            fragranceName = state.fragranceDetail!!.name,
            isLoading = state.isAddingReview,
            onDismiss = { showAddReviewDialog = false },
            onConfirm = { text, rating ->
                viewModel.addReview(text, rating)
                showAddReviewDialog = false
            }
        )
    }
}

@Composable
fun FragranceDetailContent(
    fragranceDetail: FragranceDetail,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    onAddToCollection: () -> Unit,
    onAddReview: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box {
            AsyncImage(
                model = fragranceDetail.imageUrl,
                contentDescription = fragranceDetail.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
        }
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = fragranceDetail.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "от ${fragranceDetail.brand.name}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RatingBar(
                    rating = fragranceDetail.averageRating,
                    modifier = Modifier.height(20.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "${fragranceDetail.averageRating} (${fragranceDetail.ratingCount} отзывов)",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Основные характеристики аромата
            InfoCard(fragranceDetail)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Вкладки: Описание, Ноты, Отзывы
            TabRow(
                selectedTabIndex = selectedTabIndex
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { onTabSelected(0) },
                    text = { Text("Описание") }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { onTabSelected(1) },
                    text = { Text("Ноты") }
                )
                Tab(
                    selected = selectedTabIndex == 2,
                    onClick = { onTabSelected(2) },
                    text = { Text("Отзывы") }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            when (selectedTabIndex) {
                0 -> DescriptionTab(fragranceDetail)
                1 -> NotesTab(fragranceDetail)
                2 -> ReviewsTab(
                    reviews = fragranceDetail.reviews,
                    onAddReview = onAddReview
                )
            }
        }
    }
}

@Composable
fun InfoCard(fragranceDetail: FragranceDetail) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    InfoField("Год выпуска", fragranceDetail.releaseYear?.toString() ?: "Неизвестно")
                    InfoField("Семейство", fragranceDetail.family?.name ?: "Неизвестно")
                }
                
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    InfoField("Концентрация", fragranceDetail.concentration?.name ?: "Неизвестно")
                    InfoField("Страна бренда", fragranceDetail.brand.country ?: "Неизвестно")
                }
            }
        }
    }
}

@Composable
fun InfoField(label: String, value: String) {
    Column(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun DescriptionTab(fragranceDetail: FragranceDetail) {
    Text(
        text = fragranceDetail.description ?: "Описание отсутствует",
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun NotesTab(fragranceDetail: FragranceDetail) {
    Column {
        NoteSection("Верхние ноты", fragranceDetail.topNotes)
        Spacer(modifier = Modifier.height(16.dp))
        
        NoteSection("Средние ноты", fragranceDetail.middleNotes)
        Spacer(modifier = Modifier.height(16.dp))
        
        NoteSection("Базовые ноты", fragranceDetail.baseNotes)
    }
}

@Composable
fun NoteSection(title: String, notes: String?) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = notes ?: "Нет данных",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ReviewsTab(
    reviews: List<Review>,
    onAddReview: () -> Unit
) {
    Column {
        Button(
            onClick = onAddReview,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Оставить отзыв")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (reviews.isEmpty()) {
            Text(
                text = "Отзывов пока нет. Будьте первым!",
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            reviews.forEach { review ->
                ReviewItem(review)
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = review.username,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            RatingBar(
                rating = review.rating.toFloat(),
                modifier = Modifier.height(16.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = review.text,
            style = MaterialTheme.typography.bodyMedium
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = review.createdAt,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}