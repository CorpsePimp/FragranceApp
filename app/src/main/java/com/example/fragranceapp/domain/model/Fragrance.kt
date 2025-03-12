package com.example.fragranceapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Модель данных для представления парфюмерного аромата в приложении.
 * Используется Parcelize для возможности передачи объекта между компонентами Android.
 *
 * @property id Уникальный идентификатор аромата
 * @property name Название аромата
 * @property brandId Идентификатор бренда
 * @property brandName Название бренда
 * @property releaseYear Год выпуска (может быть null, если неизвестен)
 * @property imageUrl URL изображения аромата (может быть null)
 * @property averageRating Средний рейтинг аромата от 0.0 до 5.0
 * @property ratingCount Количество оставленных оценок
 * @property concentrationType Тип концентрации (например, EDT, EDP, и т.д.)
 * @property isFavorite Флаг, показывающий добавлен ли аромат в избранное
 */
@Parcelize
data class Fragrance(
    val id: Int,
    val name: String,
    val brandId: Int,
    val brandName: String,
    val releaseYear: Int? = null,
    val imageUrl: String? = null,
    val averageRating: Float = 0.0f,
    val ratingCount: Int = 0,
    val concentrationType: String? = null,
    val isFavorite: Boolean = false
) : Parcelable {
    /**
     * Возвращает полное название аромата с брендом
     */
    fun getFullName(): String = "$brandName $name"
    
    /**
     * Возвращает отформатированную строку с годом выпуска
     */
    fun getFormattedReleaseYear(): String = releaseYear?.toString() ?: "Н/Д"
    
    /**
     * Проверяет, имеет ли аромат оценки
     */
    fun hasRatings(): Boolean = ratingCount > 0
}