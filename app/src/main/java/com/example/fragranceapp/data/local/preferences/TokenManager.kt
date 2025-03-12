package com.example.fragranceapp.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("auth_preferences")

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }

    fun getAccessToken(): String? {
        val flow: Flow<String?> = dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN]
        }
        
        // В реальном приложении стоит использовать Flow
        // Здесь используем синхронный подход для простоты
        var token: String? = null
        runCatching {
            token = flow.toString()
        }
        return token
    }

    fun getRefreshToken(): String? {
        val flow: Flow<String?> = dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN]
        }
        
        var token: String? = null
        runCatching {
            token = flow.toString()
        }
        return token
    }

    suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN)
            preferences.remove(REFRESH_TOKEN)
        }
    }
}