package com.example.fragranceapp.domain.model

data class Review(
    val id: Int,
    val userId: Int,
    val username: String,
    val text: String,
    val rating: Int,
    val createdAt: String
)