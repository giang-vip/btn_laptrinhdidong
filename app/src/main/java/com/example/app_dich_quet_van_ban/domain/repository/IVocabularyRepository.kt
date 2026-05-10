package com.example.app_dich_quet_van_ban.domain.repository

import android.content.Context
import android.net.Uri
import com.example.app_dich_quet_van_ban.data.local.entity.CardEntity
import com.example.app_dich_quet_van_ban.data.local.entity.DeckEntity
import com.example.app_dich_quet_van_ban.data.local.entity.FolderEntity
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface IVocabularyRepository {
    // Quản lý Folder
    fun getFolders(userId: Int): Flow<List<FolderEntity>>
    suspend fun addFolder(folder: FolderEntity)

    // Quản lý Deck (Bộ từ)
    fun getDecksByFolder(folderId: Int): Flow<List<DeckEntity>>
    suspend fun getDecksByFolderSync(folderId: Int): List<DeckEntity>

    // Quản lý Card (Trung tâm điều khiển)
    fun getCardsInDeck(deckId: Int): Flow<List<CardEntity>>
    suspend fun saveCard(card: CardEntity): Long
    suspend fun deleteCard(card: CardEntity)

    suspend fun importWordsFromCsv(context: Context, uri: Uri, folderId: Int)
}

