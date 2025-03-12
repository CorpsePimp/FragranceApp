package com.example.fragranceapp.data.remote.api

import com.example.fragranceapp.data.remote.dto.CollectionTypeDto
import com.example.fragranceapp.data.remote.dto.UserCollectionDto
import com.example.fragranceapp.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @GET("user/me")
    suspend fun getCurrentUser(): Response<UserDto>

    @GET("collection/types")
    suspend fun getCollectionTypes(): Response<List<CollectionTypeDto>>

    @GET("user/collections")
    suspend fun getUserCollections(
        @Query("collection_type_id") collectionTypeId: Int? = null
    ): Response<List<UserCollectionDto>>

    @POST("user/collections")
    suspend fun addToCollection(@Body request: Map<String, Any>): Response<UserCollectionDto>

    @DELETE("user/collections/{id}")
    suspend fun removeFromCollection(@Path("id") collectionId: Int): Response<Unit>
}