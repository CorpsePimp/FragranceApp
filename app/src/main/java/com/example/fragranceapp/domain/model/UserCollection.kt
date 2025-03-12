package com.example.fragranceapp.domain.model

data class UserCollection(
    val id: Int,
    val userId: Int,
    val fragranceId: Int,
    val fragrance: Fragrance,
    val collectionTypeId: Int,
    val collectionTypeName: String,
    val notes: String? = null,
    val addedAt: String
)