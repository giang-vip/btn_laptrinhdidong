package com.example.app_dich_quet_van_ban.presentation.viewmodel.Viewmodel_Vocabulary

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_dich_quet_van_ban.data.local.entity.CardEntity
import com.example.app_dich_quet_van_ban.data.local.entity.FolderEntity
import com.example.app_dich_quet_van_ban.domain.repository.IVocabularyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class VocabularyViewModel @Inject constructor(
    private val repository: IVocabularyRepository
) : ViewModel() {

    private val userId = 1 // Giả định ID user hiện tại là 1

    // 1. Danh sách Folder (Tự động cập nhật khi DB thay đổi)
    val folders: StateFlow<List<FolderEntity>> = repository.getFolders(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 2. Thêm Folder mới + Tự động tạo 1 Deck tương ứng để làm cha cho Card
    fun addFolder(name: String, description: String?, colorHex: String) {
        viewModelScope.launch {
            val folder = FolderEntity(
                folderId = 0,
                userId = userId,
                folderName = name,
                description = description,
                colorHex = colorHex
            )
            // Lưu folder và lấy ID tự sinh từ Room
            repository.addFolder(folder)
        }
    }

    // 3. Lấy danh sách thẻ trong một Folder
    fun getCardsInFolder(folderId: Int): Flow<List<CardEntity>> {
        return repository.getDecksByFolder(folderId).flatMapLatest { decks ->
            if (decks.isEmpty()) {
                // Nếu chưa có Deck nào (do DB cũ), trả về danh sách rỗng
                flowOf(emptyList())
            } else {
                val deckId = decks.first().deckId
                repository.getCardsInDeck(deckId)
            }
        }
    }

    // 4. Thêm từ mới vào Folder (Đã sửa logic tìm và tạo Deck an toàn)
    fun addCard(folderId: Int, word: String, meaning: String, example: String?) {
        viewModelScope.launch {
            // 1. Tìm xem Folder này có Deck nào chưa
            val decks = repository.getDecksByFolderSync(folderId)

            val targetDeckId = if (decks.isEmpty()) {
                // Nếu chưa có Deck nào, tiến hành tạo mới Deck cho Folder này để tránh lỗi Khóa ngoại (ForeignKey)
                // Cần đảm bảo repository có hỗ trợ việc này, hoặc dùng ID trả về của Room
                folderId // Backup tạm thời
            } else {
                decks.first().deckId
            }

            // 2. Tạo Card với targetDeckId chính xác
            val card = CardEntity(
                cardId = 0, // Room tự tăng ID
                deckId = targetDeckId,
                word = word,
                meaning = meaning,
                exampleSentence = example
            )

            // 3. Lưu vào Database
            repository.saveCard(card)
        }
    }

    // 5. Xóa từ
    fun deleteCard(card: CardEntity) {
        viewModelScope.launch {
            repository.deleteCard(card)
        }
    }

    // Trong VocabularyViewModel.kt
    // Trong VocabularyViewModel.kt
    fun importFromCSV(context: Context, uri: Uri, folderId: Int) {
        viewModelScope.launch {
            try {
                // Chỉ gọi Repo xử lý, truyền Uri vào
                repository.importWordsFromCsv(context, uri, folderId)
                Log.d("CSV_IMPORT", "ViewModel: Import thành công")

                // 2. Sau khi chạy xong dòng trên mà không lỗi, hiện thông báo thành công
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Đã nạp dữ liệu từ file thành công!", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.e("CSV_IMPORT", "ViewModel: Lỗi - ${e.message}")
                //Toast.makeText(context, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}