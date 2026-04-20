package com.example.app_dich_quet_van_ban.domain.repository

import com.example.app_dich_quet_van_ban.data.local.entity.ScannedDocEntity
import com.example.app_dich_quet_van_ban.domain.model.TranslationResult
import kotlinx.coroutines.flow.Flow

interface TranslationRepository {
    suspend fun translateText(text: String, source: String, target: String): String
    suspend fun saveToHistory(result: TranslationResult)
    fun getHistory(): kotlinx.coroutines.flow.Flow<List<TranslationResult>>
    // THÊM DÒNG NÀY:
    suspend fun getDocByTitle(fileName: String): ScannedDocEntity?
    fun searchDocs(query: String): Flow<List<ScannedDocEntity>>
    fun updateDoc(updatedDoc: com.example.app_dich_quet_van_ban.data.local.entity.ScannedDocEntity)
}