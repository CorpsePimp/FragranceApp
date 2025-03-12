package com.example.fragranceapp.util

object Constants {
    // Base URL для API
    const val BASE_URL = "https://api.fragranceapp.example.com/"
    
    // Константы для пагинации
    const val DEFAULT_PAGE_SIZE = 20
    const val INITIAL_PAGE = 1
    
    // Ключи для сохранения состояния
    const val KEY_SEARCH_QUERY = "search_query"
    const val KEY_SELECTED_BRAND = "selected_brand"
    const val KEY_SELECTED_FAMILY = "selected_family"
    
    // Константы для переходов между экранами
    const val ARG_FRAGRANCE_ID = "fragranceId"
    const val ARG_COLLECTION_TYPE_ID = "collectionTypeId"
}