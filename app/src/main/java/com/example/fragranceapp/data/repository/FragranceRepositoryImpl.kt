package com.example.fragranceapp.data.repository

import com.example.fragranceapp.data.local.dao.FragranceDao
import com.example.fragranceapp.data.local.entity.FragranceEntity
import com.example.fragranceapp.data.remote.api.FragranceApi
import com.example.fragranceapp.domain.model.Fragrance
import com.example.fragranceapp.domain.model.FragranceDetail
import com.example.fragranceapp.domain.repository.FragranceRepository
import com.example.fragranceapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FragranceRepositoryImpl @Inject constructor(
    private val fragranceApi: FragranceApi,
    private val fragranceDao: FragranceDao
) : FragranceRepository {

    override suspend fun getFragrances(
        search: String?,
        brandId: Int?,
        familyId: Int?,
        concentrationId: Int?,
        note: String?,
        minRating: Float?,
        year: Int?
    ): Resource<List<Fragrance>> {
        return try {
            val response = fragranceApi.getFragrances(
                search = search,
                brandId = brandId,
                familyId = familyId,
                concentrationId = concentrationId,
                note = note,
                minRating = minRating,
                year = year
            )

            if (response.isSuccessful) {
                val fragrances = response.body()?.items?.map { it.toFragrance() } ?: emptyList()
                
                // Кэшируем результат в базу данных
                fragranceDao.insertFragrances(fragrances.map { FragranceEntity.fromFragrance(it) })
                
                Resource.Success(fragrances)
            } else {
                Resource.Error("Ошибка: ${response.code()} ${response.message()}")
            }
        } catch (e: HttpException) {
            Resource.Error("Ошибка сети: ${e.message()}")
        } catch (e: IOException) {
            Resource.Error("Проверьте подключение к интернету")
        }
    }

    override suspend fun getFragranceDetail(id: Int): Resource<FragranceDetail> {
        return try {
            val response = fragranceApi.getFragranceDetail(id)

            if (response.isSuccessful) {
                val fragrance = response.body()?.toFragranceDetail()
                if (fragrance != null) {
                    Resource.Success(fragrance)
                } else {
                    Resource.Error("Информация об аромате не найдена")
                }
            } else {
                Resource.Error("Ошибка: ${response.code()} ${response.message()}")
            }
        } catch (e: HttpException) {
            Resource.Error("Ошибка сети: ${e.message()}")
        } catch (e: IOException) {
            Resource.Error("Проверьте подключение к интернету")
        }
    }

    override fun getCachedFragrances(): Flow<List<Fragrance>> {
        return fragranceDao.getFragrances().map { entities ->
            entities.map { it.toFragrance() }
        }
    }
}