package com.example.fragranceapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fragranceapp.data.local.entity.UserCollectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserCollectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserCollections(collections: List<UserCollectionEntity>)

    @Query("SELECT * FROM user_collections WHERE collectionTypeId = :collectionTypeId OR :collectionTypeId IS NULL")
    fun getUserCollections(collectionTypeId: Int? = null): Flow<List<UserCollectionEntity>>

    @Query("DELETE FROM user_collections WHERE id = :id")
    suspend fun deleteUserCollection(id: Int)

    @Query("DELETE FROM user_collections")
    suspend fun clearUserCollections()
}