package com.example.fragranceapp.domain.repository

import com.example.fragranceapp.util.Resource

interface AuthRepository {
    suspend fun login(username: String, password: String): Resource<Unit>

    suspend fun register(
        username: String,
        email: String,
        password: String,
        firstName: String?,
        lastName: String?
    ): Resource<Unit>

    suspend fun refreshToken(): Resource<Unit>

    suspend fun logout()
}