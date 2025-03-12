package com.example.fragranceapp.domain.model

data class Brand(
    val id: Int,
    val name: String,
    val description: String? = null,
    val foundedYear: Int? = null,
    val country: String? = null
)