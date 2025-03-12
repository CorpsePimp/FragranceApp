package com.example.fragranceapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Fragrance(
    val id: Int,
    val name: String,
    val brandId: Int,
    val brandName: String,
    val releaseYear: Int?,
    val imageUrl: String?,
    val averageRating: Float,
    val ratingCount: Int
) : Parcelable