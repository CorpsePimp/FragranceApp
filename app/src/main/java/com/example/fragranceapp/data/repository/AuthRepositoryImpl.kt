package com.example.fragranceapp.data.repository

import com.example.fragranceapp.data.local.preferences.TokenManager
import com.example.fragranceapp.data.remote.api.AuthApi
import com.example.fragranceapp.data.remote.dto.LoginRequestDto
import com.example.fragranceapp.data.remote.dto.RegisterRequestDto
import com.example.fragranceapp.domain.repository.AuthRepository
import com.example.fragranceapp.util.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(username: String, password: String): Resource<Unit> {
        return try {
            val response = authApi.login(
                LoginRequestDto(
                    username = username,
                    password = password
                )
            )

            if (response.isSuccessful) {
                val tokenResponse = response.body()
                if (tokenResponse != null) {
                    tokenManager.saveTokens(
                        accessToken = tokenResponse.accessToken,
                        refreshToken = tokenResponse.refreshToken
                    )
                    Resource.Success(Unit)
                } else {
                    Resource.Error("Некорректный ответ от сервера")
                }
            } else {
                Resource.Error("Ошибка входа: ${response.code()} ${response.message()}")
            }
        } catch (e: HttpException) {
            Resource.Error("Ошибка сети: ${e.message()}")
        } catch (e: IOException) {
            Resource.Error("Проверьте подключение к интернету")
        }
    }

    override suspend fun register(
        username: String,
        email: String,
        password: String,
        firstName: String?,
        lastName: String?
    ): Resource<Unit> {
        return try {
            val response = authApi.register(
                RegisterRequestDto(
                    username = username,
                    email = email,
                    password = password,
                    firstName = firstName,
                    lastName = lastName
                )
            )

            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Ошибка регистрации: ${response.code()} ${response.message()}")
            }
        } catch (e: HttpException) {
            Resource.Error("Ошибка сети: ${e.message()}")
        } catch (e: IOException) {
            Resource.Error("Проверьте подключение к интернету")
        }
    }

    override suspend fun refreshToken(): Resource<Unit> {
        return try {
            val response = authApi.refreshToken()

            if (response.isSuccessful) {
                val tokenResponse = response.body()
                if (tokenResponse != null) {
                    tokenManager.saveTokens(
                        accessToken = tokenResponse.accessToken,
                        refreshToken = tokenResponse.refreshToken
                    )
                    Resource.Success(Unit)
                } else {
                    Resource.Error("Некорректный ответ от сервера")
                }
            } else {
                Resource.Error("Ошибка обновления токена: ${response.code()} ${response.message()}")
            }
        } catch (e: HttpException) {
            Resource.Error("Ошибка сети: ${e.message()}")
        } catch (e: IOException) {
            Resource.Error("Проверьте подключение к интернету")
        }
    }

    override suspend fun logout() {
        try {
            authApi.logout()
        } finally {
            tokenManager.clearTokens()
        }
    }
}