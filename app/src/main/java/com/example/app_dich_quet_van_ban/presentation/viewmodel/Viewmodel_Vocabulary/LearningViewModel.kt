package com.example.app_dich_quet_van_ban.presentation.viewmodel.Viewmodel_Vocabulary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_dich_quet_van_ban.data.local.entity.CardEntity
import com.example.app_dich_quet_van_ban.domain.repository.ILearningRepository
import com.example.app_dich_quet_van_ban.domain.repository.IVocabularyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LearningViewModel @Inject constructor(
    private val learningRepo: ILearningRepository,
    private val vocabRepo: IVocabularyRepository
) : ViewModel() {

    private val _cards = MutableStateFlow<List<CardEntity>>(emptyList())
    val cards = _cards.asStateFlow()

    // Tải danh sách từ cần học
    fun loadCards(folderId: Int) {
        viewModelScope.launch {
            vocabRepo.getCardsInDeck(folderId).collect {
                _cards.value = it
            }
        }
    }

    // Xử lý khi người dùng nhấn "Next" (Đánh giá mức độ thuộc bài)
    fun rateCard(cardId: Int, rating: Int) {
        viewModelScope.launch {
            learningRepo.processReview(cardId, 1, rating)
        }
    }
}