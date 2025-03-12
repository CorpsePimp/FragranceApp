package com.example.fragranceapp.domain.model

data class FragranceDetail(
    val id: Int,
    val name: String,
    val brand: Brand,
    val releaseYear: Int?,
    val family: Family?,
    val concentration: Concentration?,
    val topNotes: String?,
    val middleNotes: String?,
    val baseNotes: String?,
    val description: String?,
    val imageUrl: String?,
    val averageRating: Float,
    val ratingCount: Int,
    val reviews: List<Review>
)

data class Brand(
    val id: Int,
    val name: String,
    val description: String?,
    val foundedYear: Int?,
    val country: String?
)

data class Family(
    val id: Int,
    val name: String,
    val description: String?
)

data class Concentration(
    val id: Int,
    val name: String,
    val description: String?
)

data class Review(
    val id: Int,
    val userId: Int,
    val username: String,
    val text: String,
    val rating: Int,
    val createdAt: String
)