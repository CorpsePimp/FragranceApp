package com.example.fragranceapp.data.remote.dto

import com.example.fragranceapp.domain.model.Brand
import com.example.fragranceapp.domain.model.Concentration
import com.example.fragranceapp.domain.model.Family
import com.example.fragranceapp.domain.model.Fragrance
import com.example.fragranceapp.domain.model.FragranceDetail
import com.example.fragranceapp.domain.model.Review

data class FragranceListDto(
    val items: List<FragranceDto>,
    val totalCount: Int,
    val pageCount: Int
)

data class FragranceDto(
    val id: Int,
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
}

data class FragranceDetailDto(
    val id: Int,
    val name: String,
    val brand: BrandDto,
    val releaseYear: Int?,
    val family: FamilyDto?,
    val concentration: ConcentrationDto?,
    val topNotes: String?,
    val middleNotes: String?,
    val baseNotes: String?,
    val description: String?,
    val imageUrl: String?,
    val averageRating: Float,
    val ratingCount: Int,
    val reviews: List<ReviewDto>
) {
    fun toFragranceDetail(): FragranceDetail {
        return FragranceDetail(
            id = id,
            name = name,
            brand = brand.toBrand(),
            releaseYear = releaseYear,
            family = family?.toFamily(),
            concentration = concentration?.toConcentration(),
            topNotes = topNotes,
            middleNotes = middleNotes,
            baseNotes = baseNotes,
            description = description,
            imageUrl = imageUrl,
            averageRating = averageRating,
            ratingCount = ratingCount,
            reviews = reviews.map { it.toReview() }
        )
    }
}

data class BrandDto(
    val id: Int,
    val name: String,
    val description: String?,
    val foundedYear: Int?,
    val country: String?
) {
    fun toBrand(): Brand {
        return Brand(
            id = id,
            name = name,
            description = description,
            foundedYear = foundedYear,
            country = country
        )
    }
}

data class FamilyDto(
    val id: Int,
    val name: String,
    val description: String?
) {
    fun toFamily(): Family {
        return Family(
            id = id,
            name = name,
            description = description
        )
    }
}

data class ConcentrationDto(
    val id: Int,
    val name: String,
    val description: String?
) {
    fun toConcentration(): Concentration {
        return Concentration(
            id = id,
            name = name,
            description = description
        )
    }
}

data class ReviewDto(
    val id: Int,
    val userId: Int,
    val username: String,
    val text: String,
    val rating: Int,
    val createdAt: String
) {
    fun toReview(): Review {
        return Review(
            id = id,
            userId = userId,
            username = username,
            text = text,
            rating = rating,
            createdAt = createdAt
        )
    }
}