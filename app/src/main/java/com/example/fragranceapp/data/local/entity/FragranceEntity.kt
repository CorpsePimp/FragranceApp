package com.example.fragranceapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.fragranceapp.domain.model.Fragrance

@Entity(tableName = "fragrances")
data class FragranceEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val brandId: Int,
    val brandName: String,
    val releaseYear: Int?,
    val imageUrl: String?,
    val averageRating: Float,
    val ratingCount: Int
) {
    fun toFragrance(): Fragrance {
        return Fragrance(
            id = id,
            name = name,
            brandId = brandId,
            brandName = brandName,
            releaseYear = releaseYear,
            imageUrl = imageUrl,
            averageRating = averageRating,
            ratingCount = ratingCount
        )
    }

    companion object {
        fun fromFragrance(fragrance: Fragrance): FragranceEntity {
            return FragranceEntity(
                id = fragrance.id,
                name = fragrance.name,
                brandId = fragrance.brandId,
                brandName = fragrance.brandName,
                releaseYear = fragrance.releaseYear,
                imageUrl = fragrance.imageUrl,
                averageRating = fragrance.averageRating,
                ratingCount = fragrance.ratingCount
            )
        }
    }
}