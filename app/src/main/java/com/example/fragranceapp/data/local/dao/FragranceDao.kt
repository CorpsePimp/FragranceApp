package com.example.fragranceapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fragranceapp.data.local.entity.FragranceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FragranceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFragrances(fragrances: List<FragranceEntity>)

    @Query("SELECT * FROM fragrances")
    fun getFragrances(): Flow<List<FragranceEntity>>

    @Query("SELECT * FROM fragrances WHERE id = :id")
    suspend fun getFragranceById(id: Int): FragranceEntity?

    @Query("DELETE FROM fragrances")
    suspend fun clearFragrances()
}