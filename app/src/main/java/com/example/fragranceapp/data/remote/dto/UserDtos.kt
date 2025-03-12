package com.example.fragranceapp.data.remote.dto

import com.example.fragranceapp.domain.model.CollectionType
import com.example.fragranceapp.domain.model.User
import com.example.fragranceapp.domain.model.UserCollection

data class UserDto(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String?,
    val lastName: String?
) {
    fun toUser(): User {
        return User(
            id = id,
            username = username,
            email = email,
            firstName = firstName,
            lastName = lastName
        )
    }
}

data class CollectionTypeDto(
    val id: Int,
    val name: String,
    val description: String?
) {
    fun toCollectionType(): CollectionType {
        return CollectionType(
            id = id,
            name = name,
            description = description
        )
    }
}

data class UserCollectionDto(
    val id: Int,
    val userId: Int,
    val fragranceId: Int,
    val fragrance: FragranceDto,
    val collectionTypeId: Int,
    val collectionTypeName: String,
    val notes: String?,
    val addedAt: String
) {
    fun toUserCollection(): UserCollection {
        return UserCollection(
            id = id,
            userId = userId,
            fragranceId = fragranceId,
            fragrance = fragrance.toFragrance(),
            collectionTypeId = collectionTypeId,
            collectionTypeName = collectionTypeName,
            notes = notes,
            addedAt = addedAt
        )
    }
}