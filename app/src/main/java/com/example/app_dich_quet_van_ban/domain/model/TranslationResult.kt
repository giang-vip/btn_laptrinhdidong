package com.example.app_dich_quet_van_ban.domain.model

data class TranslationResult(
    val id: Int = 0,
    val originalText: String,
    val translatedText: String,
    val sourceLang: String,
    val targetLang: String,
    val timestamp: Long = System.currentTimeMillis()
)
