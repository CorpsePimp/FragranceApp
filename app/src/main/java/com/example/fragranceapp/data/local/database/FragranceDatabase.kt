package com.example.fragranceapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fragranceapp.data.local.dao.FragranceDao
import com.example.fragranceapp.data.local.dao.UserCollectionDao
import com.example.fragranceapp.data.local.entity.FragranceEntity
import com.example.fragranceapp.data.local.entity.UserCollectionEntity

@Database(
    entities = [FragranceEntity::class, UserCollectionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FragranceDatabase : RoomDatabase() {
    abstract fun fragranceDao(): FragranceDao
    abstract fun userCollectionDao(): UserCollectionDao
}