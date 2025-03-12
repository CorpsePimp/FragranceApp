package com.example.fragranceapp.di

import android.content.Context
import androidx.room.Room
import com.example.fragranceapp.data.local.database.FragranceDatabase
import com.example.fragranceapp.data.local.preferences.TokenManager
import com.example.fragranceapp.data.local.preferences.UserPreferences
import com.example.fragranceapp.data.remote.api.AuthApi
import com.example.fragranceapp.data.remote.api.FragranceApi
import com.example.fragranceapp.data.remote.api.UserApi
import com.example.fragranceapp.data.remote.interceptors.AuthInterceptor
import com.example.fragranceapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManager(context)
    }

    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context)
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor {
        return AuthInterceptor(tokenManager)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFragranceApi(retrofit: Retrofit): FragranceApi {
        return retrofit.create(FragranceApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFragranceDatabase(@ApplicationContext context: Context): FragranceDatabase {
        return Room.databaseBuilder(
            context,
            FragranceDatabase::class.java,
            "fragrance_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideFragranceDao(database: FragranceDatabase) = database.fragranceDao()

    @Provides
    @Singleton
    fun provideUserCollectionDao(database: FragranceDatabase) = database.userCollectionDao()
}