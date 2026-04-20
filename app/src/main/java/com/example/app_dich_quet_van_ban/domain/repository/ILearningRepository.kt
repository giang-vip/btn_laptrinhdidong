package com.example.app_dich_quet_van_ban.domain.repository

import com.example.app_dich_quet_van_ban.data.local.entity.CardEntity
import kotlinx.coroutines.flow.Flow

interface ILearningRepository {
    // Lấy danh sách thẻ đến hạn học hôm nay
    fun getDueCards(userId: Int): Flow<List<CardEntity>>

    // Xử lý khi người dùng nhấn nút đánh giá (Again, Hard, Good, Easy)
    suspend fun processReview(cardId: Int, userId: Int, rating: Int)
}