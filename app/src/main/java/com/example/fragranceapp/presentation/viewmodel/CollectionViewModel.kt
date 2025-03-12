package com.example.fragranceapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragranceapp.domain.model.CollectionType
import com.example.fragranceapp.domain.model.UserCollection
import com.example.fragranceapp.domain.repository.UserRepository
import com.example.fragranceapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CollectionState())
    val state: StateFlow<CollectionState> = _state.asStateFlow()

    init {
        loadCollectionTypes()
        loadUserCollections()
    }

    private fun loadCollectionTypes() {
        viewModelScope.launch {
            val result = userRepository.getCollectionTypes()
            if (result is Resource.Success) {
                _state.update { 
                    it.copy(
                        collectionTypes = result.data ?: emptyList(),
                        selectedCollectionType = result.data?.firstOrNull()
                    )
                }
            }
        }
    }

    fun loadUserCollections() {
        _state.update { it.copy(isLoading = true, error = null) }
        
        viewModelScope.launch {
            val result = userRepository.getUserCollections(
                collectionTypeId = _state.value.selectedCollectionType?.id
            )
            
            when (result) {
                is Resource.Success -> {
                    _state.update { 
                        it.copy(
                            collections = result.data ?: emptyList(),
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

    fun selectCollectionType(collectionType: CollectionType) {
        _state.update { it.copy(selectedCollectionType = collectionType) }
        loadUserCollections()
    }

    fun removeFromCollection(collectionId: Int) {
        viewModelScope.launch {
            val result = userRepository.removeFromCollection(collectionId)
            if (result is Resource.Success) {
                loadUserCollections()
            } else if (result is Resource.Error) {
                _state.update { it.copy(error = result.message) }
            }
        }
    }
}

data class CollectionState(
    val collectionTypes: List<CollectionType> = emptyList(),
    val selectedCollectionType: CollectionType? = null,
    val collections: List<UserCollection> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)