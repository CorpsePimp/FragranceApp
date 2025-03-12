package com.example.fragranceapp.domain.model

data class FragranceDetail(
    val id: Int,
    val name: String,
    val brand: Brand,
    val releaseYear: Int? = null,
    val family: Family? = null,
    val concentration: Concentration? = null,
    val topNotes: String? = null,
    val middleNotes: String? = null,
    val baseNotes: String? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val averageRating: Float,
    val ratingCount: Int,
    val reviews: List<Review> = emptyList()
)