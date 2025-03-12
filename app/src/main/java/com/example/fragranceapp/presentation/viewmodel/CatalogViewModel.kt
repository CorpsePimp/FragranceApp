package com.example.fragranceapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragranceapp.domain.repository.FragranceRepository
import com.example.fragranceapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val fragranceRepository: FragranceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CatalogState())
    val state = _state.asStateFlow()

    init {
        loadFragrances()
    }

    fun loadFragrances(
        search: String? = null,
        brandId: Int? = null,
        familyId: Int? = null,
        concentrationId: Int? = null,
        note: String? = null,
        minRating: Float? = null,
        year: Int? = null
    ) {
        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = fragranceRepository.getFragrances(
                search = search,
                brandId = brandId,
                familyId = familyId,
                concentrationId = concentrationId,
                note = note,
                minRating = minRating,
                year = year
            )

            when (result) {
                is Resource.Success -> {
                    _state.update { 
                        it.copy(
                            fragrances = result.data ?: emptyList(),
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

    fun applyFilters() {
        val searchQuery = _state.value.searchQuery.takeIf { it.isNotBlank() }
        val brandId = _state.value.selectedBrandId
        val familyId = _state.value.selectedFamilyId
        val concentrationId = _state.value.selectedConcentrationId
        val minRating = _state.value.selectedMinRating
        val year = _state.value.selectedYear

        loadFragrances(
            search = searchQuery,
            brandId = brandId,
            familyId = familyId,
            concentrationId = concentrationId,
            minRating = minRating,
            year = year
        )
    }

    fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
        if (query.length >= 3 || query.isEmpty()) {
            applyFilters()
        }
    }

    fun updateSelectedBrandId(brandId: Int?) {
        _state.update { it.copy(selectedBrandId = brandId) }
    }

    fun updateSelectedFamilyId(familyId: Int?) {
        _state.update { it.copy(selectedFamilyId = familyId) }
    }

    fun updateSelectedConcentrationId(concentrationId: Int?) {
        _state.update { it.copy(selectedConcentrationId = concentrationId) }
    }

    fun updateSelectedMinRating(rating: Float?) {
        _state.update { it.copy(selectedMinRating = rating) }
    }

    fun updateSelectedYear(year: Int?) {
        _state.update { it.copy(selectedYear = year) }
    }
}

data class CatalogState(
    val fragrances: List<Fragrance> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedBrandId: Int? = null,
    val selectedFamilyId: Int? = null,
    val selectedConcentrationId: Int? = null,
    val selectedMinRating: Float? = null,
    val selectedYear: Int? = null
)