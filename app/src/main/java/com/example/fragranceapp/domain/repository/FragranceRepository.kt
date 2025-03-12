package com.example.fragranceapp.domain.repository

import com.example.fragranceapp.domain.model.*
import com.example.fragranceapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface FragranceRepository {
    suspend fun getFragrances(
        search: String? = null,
        brandId: Int? = null,
        familyId: Int? = null,
        concentrationId: Int? = null,
        note: String? = null,
        minRating: Float? = null,
        year: Int? = null
    ): Resource<List<Fragrance>>

    suspend fun getFragranceDetail(id: Int): Resource<FragranceDetail>

    fun getCachedFragrances(): Flow<List<Fragrance>>
}