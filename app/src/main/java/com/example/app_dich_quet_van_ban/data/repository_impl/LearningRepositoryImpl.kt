package com.example.app_dich_quet_van_ban.data.repository_impl

import com.example.app_dich_quet_van_ban.data.local.dao.CardDao
import com.example.app_dich_quet_van_ban.data.local.dao.HistoryDao
import com.example.app_dich_quet_van_ban.data.local.dao.ReviewDao
import com.example.app_dich_quet_van_ban.data.local.entity.CardEntity
import com.example.app_dich_quet_van_ban.data.local.entity.HistoryEntity
import com.example.app_dich_quet_van_ban.data.local.entity.ReviewEntity
import com.example.app_dich_quet_van_ban.domain.repository.ILearningRepository
import kotlinx.coroutines.flow.Flow

class LearningRepositoryImpl(
    private val reviewDao: ReviewDao,
    private val historyDao: HistoryDao
) : ILearningRepository {

    override fun getDueCards(userId: Int): Flow<List<CardEntity>> {
        return reviewDao.getCardsToReview(userId, System.currentTimeMillis())
    }

    override suspend fun processReview(cardId: Int, userId: Int, rating: Int) {
        // 1. Lấy trạng thái hiện tại hoặc tạo mới nếu lần đầu học
        val currentReview = reviewDao.getReviewStatus(cardId, userId)
            ?: ReviewEntity(card_id = cardId, user_id = userId, next_review_timestamp = 0)

        var n = currentReview.repetition
        var ef = currentReview.ease_factor
        var interval = currentReview.interval

        // 2. Thuật toán SM-2 Logic
        if (rating >= 3) { // Good hoặc Easy
            when (n) {
                0 -> interval = 1
                1 -> interval = 6
                else -> interval = (interval * ef).toInt()
            }
            n++
        } else { // Again hoặc Hard
            n = 0
            interval = 1
        }

        // Cập nhật Ease Factor (Độ dễ)
        ef = ef + (0.1 - (5 - rating) * (0.08 + (5 - rating) * 0.02))
        if (ef < 1.3) ef = 1.3

        val nextReview = System.currentTimeMillis() + (interval * 24 * 60 * 60 * 1000L)

        // 3. Cập nhật vào Database (Ghi đè Review và Thêm mới History)
        reviewDao.upsertReview(currentReview.copy(
            repetition = n,
            ease_factor = ef,
            interval = interval,
            next_review_timestamp = nextReview,
            status = if (n > 4) "REVIEW" else "LEARNING"
        ))

        historyDao.insertHistory(
            HistoryEntity(
                cardId = cardId,
                userId = userId,
                rating = rating
            )
        )
    }
}