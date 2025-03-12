package com.example.fragranceapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.fragranceapp.domain.model.Fragrance
import com.example.fragranceapp.domain.model.UserCollection

@Entity(
    tableName = "user_collections",
    foreignKeys = [
        ForeignKey(
            entity = FragranceEntity::class,
            parentColumns = ["id"],
            childColumns = ["fragranceId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserCollectionEntity(
    @PrimaryKey val id: Int,
    val userId: Int,
    val fragranceId: Int,
    val collectionTypeId: Int,
    val collectionTypeName: String,
    val notes: String?,
    val addedAt: String
) {
    fun toUserCollection(fragrance: Fragrance): UserCollection {
        return UserCollection(
            id = id,
            userId = userId,
            fragranceId = fragranceId,
            fragrance = fragrance,
            collectionTypeId = collectionTypeId,
            collectionTypeName = collectionTypeName,
            notes = notes,
            addedAt = addedAt
        )
    }

    companion object {
        fun fromUserCollection(collection: UserCollection): UserCollectionEntity {
            return UserCollectionEntity(
                id = collection.id,
                userId = collection.userId,
                fragranceId = collection.fragranceId,
                collectionTypeId = collection.collectionTypeId,
                collectionTypeName = collection.collectionTypeName,
                notes = collection.notes,
                addedAt = collection.addedAt
            )
        }
    }
}