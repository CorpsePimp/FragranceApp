package com.example.fragranceapp.domain.model

import java.util.Date

/**
 * Модель данных для элемента коллекции пользователя
 *
 * @property id Уникальный идентификатор элемента коллекции
 * @property userId Идентификатор пользователя-владельца коллекции
 * @property fragranceId Идентификатор аромата
 * @property fragrance Полная информация об аромате
 * @property collectionTypeId Идентификатор типа коллекции
 * @property collectionTypeName Название типа коллекции
 * @property notes Заметки пользователя о данном аромате (может быть null)
 * @property addedAt Дата добавления аромата в коллекцию
 */
data class UserCollection(
    val id: Int,
    val userId: Int,
    val fragranceId: Int,
    val fragrance: Fragrance,
    val collectionTypeId: Int,
    val collectionTypeName: String,
    val notes: String? = null,
    val addedAt: String
)