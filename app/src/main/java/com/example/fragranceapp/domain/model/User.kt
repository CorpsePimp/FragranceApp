package com.example.fragranceapp.domain.model

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String?,
    val lastName: String?
)