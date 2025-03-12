package com.example.fragranceapp.domain.model

data class Fragrance(
    val id: Int,
    val name: String,
    val brandId: Int,
    val brandName: String,
    val releaseYear: Int? = null,
    val imageUrl: String? = null,
    val averageRating: Float = 0.0f,
    val ratingCount: Int = 0,
    val concentrationType: String? = null,
    val isFavorite: Boolean = false
)