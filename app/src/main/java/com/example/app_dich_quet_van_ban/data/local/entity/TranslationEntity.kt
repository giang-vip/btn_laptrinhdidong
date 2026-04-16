package com.example.app_dich_quet_van_ban.data.local.entity

@androidx.room.Entity(tableName = "translation_history")
data class TranslationEntity(
    @androidx.room.PrimaryKey(autoGenerate = true) val id: Int = 0,
    val originalText: String,
    val translatedText: String,
    val timestamp: Long
)
