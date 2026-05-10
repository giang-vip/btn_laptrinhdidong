package com.example.app_dich_quet_van_ban.data.repository_impl

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.app_dich_quet_van_ban.data.local.dao.CardDao
import com.example.app_dich_quet_van_ban.data.local.dao.DeckDao
import com.example.app_dich_quet_van_ban.data.local.dao.FolderDao
import com.example.app_dich_quet_van_ban.data.local.entity.CardEntity
import com.example.app_dich_quet_van_ban.data.local.entity.DeckEntity
import com.example.app_dich_quet_van_ban.data.local.entity.FolderEntity
import com.example.app_dich_quet_van_ban.domain.repository.IVocabularyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.InputStream
import javax.inject.Inject

class VocabularyRepositoryImpl @Inject constructor(
    private val folderDao: FolderDao,
    private val deckDao: DeckDao,
    private val cardDao: CardDao
) : IVocabularyRepository {

    // --- QUẢN LÝ FOLDER ---
    override fun getFolders(userId: Int): Flow<List<FolderEntity>> {
        return folderDao.getFoldersByUser(userId)
    }

    override suspend fun addFolder(folder: FolderEntity) {
        // 1. Chèn folder và lấy về Folder ID vừa tạo
        val folderId = folderDao.insert(folder)

        // 2. Tự động tạo thêm 1 Deck mặc định cho Folder này để làm khóa ngoại an toàn
        deckDao.insertDeck(
            DeckEntity(
                deckId = 0,
                folderId = folderId.toInt(),
                deckName = folder.folderName + " Deck",
                createdAt = System.currentTimeMillis()
            )
        )
    }

    // --- QUẢN LÝ DECK (BỘ TỪ) ---
    override fun getDecksByFolder(folderId: Int): Flow<List<DeckEntity>> {
        return deckDao.getDecksByFolder(folderId)
    }

    override suspend fun getDecksByFolderSync(folderId: Int): List<DeckEntity> {
        // Vì deckDao.getDecksByFolder đang trả về Flow,
        // ta dùng .first() để lấy danh sách hiện tại rồi kết thúc luồng.
        return deckDao.getDecksByFolder(folderId).first()
    }
    // --- QUẢN LÝ CARD (COMMAND CENTER) ---
    override fun getCardsInDeck(deckId: Int): Flow<List<CardEntity>> {
        return cardDao.getCardsByDeckId(deckId)
    }



    override suspend fun saveCard(card: CardEntity): Long {
        return cardDao.insertCard(card)
    }

    override suspend fun deleteCard(card: CardEntity) {
        cardDao.deleteCard(card)
    }

    // Trong VocabularyRepositoryImpl.kt

    // Trong VocabularyRepositoryImpl.kt
    override suspend fun importWordsFromCsv(context: Context, uri: Uri, folderId: Int) {
        // 1. Lấy DeckId an toàn (Chạy trên Dispatchers.IO)
        val decks = getDecksByFolderSync(folderId)
        val targetDeckId = decks.firstOrNull()?.deckId
            ?: throw IllegalStateException("Không tìm thấy Deck cho thư mục này!")

        withContext(Dispatchers.IO) {
            try {
                // 2. Mở InputStream từ Uri thông qua ContentResolver
                context.contentResolver.openInputStream(uri)?.bufferedReader(Charsets.UTF_8)?.use { reader ->
                    val lines = reader.readLines()

                    for (index in lines.indices) {
                        // Bỏ qua dòng tiêu đề (index 0) và dòng trống
                        if (index == 0 || lines[index].isBlank()) continue

                        val line = lines[index]

                        // 3. Regex đỉnh cao: Tách dấu phẩy nhưng bỏ qua dấu phẩy trong ngoặc kép ""
                        val parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)".toRegex())

                        if (parts.size >= 2) {
                            // Làm sạch dữ liệu: bỏ ngoặc kép dư thừa và khoảng trắng
                            val word = parts[0].replace("\"", "").trim()
                            val meaning = parts[1].replace("\"", "").trim()
                            val example = if (parts.size > 2) parts[2].replace("\"", "").trim() else ""

                            if (word.isNotEmpty() && meaning.isNotEmpty()) {
                                val card = CardEntity(
                                    cardId = 0,
                                    deckId = targetDeckId,
                                    word = word,
                                    meaning = meaning,
                                    exampleSentence = example,
                                )
                                cardDao.insertCard(card)
                            }
                        }
                    }
                }
                Log.d("CSV_IMPORT", "Import thành công từ file: $uri")
            } catch (e: Exception) {
                Log.e("CSV_IMPORT", "Lỗi xử lý file CSV: ${e.message}")
                throw e
            }
        }
    }
}
