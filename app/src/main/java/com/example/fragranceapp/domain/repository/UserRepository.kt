package com.example.fragranceapp.domain.repository

import com.example.fragranceapp.domain.model.CollectionType
import com.example.fragranceapp.domain.model.User
import com.example.fragranceapp.domain.model.UserCollection
import com.example.fragranceapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getCurrentUser(): Resource<User>
    
    suspend fun getCollectionTypes(): Resource<List<CollectionType>>
    
    suspend fun getUserCollections(collectionTypeId: Int? = null): Resource<List<UserCollection>>
    
    suspend fun addToCollection(
        fragranceId: Int,
        collectionTypeId: Int,
        notes: String?
    ): Resource<UserCollection>
    
    suspend fun removeFromCollection(collectionId: Int): Resource<Unit>
    
    fun getCollectionFlow(collectionTypeId: Int? = null): Flow<List<UserCollection>>
}