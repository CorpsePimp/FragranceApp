package com.example.fragranceapp.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragranceapp.domain.model.FragranceDetail
import com.example.fragranceapp.domain.model.Review
import com.example.fragranceapp.domain.repository.FragranceRepository
import com.example.fragranceapp.domain.repository.UserRepository
import com.example.fragranceapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragranceDetailViewModel @Inject constructor(
    private val fragranceRepository: FragranceRepository,
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val fragranceId: Int = checkNotNull(savedStateHandle["fragranceId"])

    private val _state = MutableStateFlow(FragranceDetailState())
    val state = _state.asStateFlow()

    init {
        loadFragranceDetails()
        loadCollectionTypes()
    }

    fun loadFragranceDetails() {
        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = fragranceRepository.getFragranceDetail(fragranceId)

            when (result) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            fragranceDetail = result.data,
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

    private fun loadCollectionTypes() {
        viewModelScope.launch {
            val result = userRepository.getCollectionTypes()
            
            if (result is Resource.Success) {
                _state.update { it.copy(collectionTypes = result.data ?: emptyList()) }
            }
        }
    }

    fun addToCollection(collectionTypeId: Int, notes: String?) {
        _state.update { it.copy(isAddingToCollection = true) }

        viewModelScope.launch {
            val result = userRepository.addToCollection(
                fragranceId = fragranceId,
                collectionTypeId = collectionTypeId,
                notes = notes
            )

            _state.update { 
                it.copy(
                    isAddingToCollection = false,
                    addToCollectionError = if (result is Resource.Error) result.message else null,
                    showAddToCollectionSuccess = result is Resource.Success
                )
            }
        }
    }

    fun addReview(text: String, rating: Int) {
        _state.update { it.copy(isAddingReview = true) }

        viewModelScope.launch {
            // В данном примере функционал добавления отзыва отсутствует
            // Здесь должен быть вызов соответствующего метода репозитория
            
            // Заглушка для демонстрации
            _state.update { 
                it.copy(
                    isAddingReview = false,
                    showAddReviewSuccess = true
                )
            }
            
            // После успешного добавления отзыва обновляем детали аромата
            loadFragranceDetails()
        }
    }

    fun resetAddToCollectionMessages() {
        _state.update { 
            it.copy(
                addToCollectionError = null,
                showAddToCollectionSuccess = false
            )
        }
    }

    fun resetAddReviewMessages() {
        _state.update { it.copy(showAddReviewSuccess = false) }
    }
}

data class FragranceDetailState(
    val fragranceDetail: FragranceDetail? = null,
    val collectionTypes: List<com.example.fragranceapp.domain.model.CollectionType> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAddingToCollection: Boolean = false,
    val addToCollectionError: String? = null,
    val showAddToCollectionSuccess: Boolean = false,
    val isAddingReview: Boolean = false,
    val showAddReviewSuccess: Boolean = false
)