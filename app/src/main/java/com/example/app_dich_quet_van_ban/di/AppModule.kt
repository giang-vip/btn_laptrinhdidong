package com.example.app_dich_quet_van_ban.di

import android.content.Context
import com.example.app_dich_quet_van_ban.data.local.AppDatabase
import com.example.app_dich_quet_van_ban.data.local.dao.*
import com.example.app_dich_quet_van_ban.data.repository_impl.LearningRepositoryImpl
import com.example.app_dich_quet_van_ban.data.repository_impl.VocabularyRepositoryImpl
import com.example.app_dich_quet_van_ban.domain.repository.ILearningRepository
import com.example.app_dich_quet_van_ban.domain.repository.IVocabularyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        // Luôn sử dụng getDatabase thông qua Companion object để đảm bảo callback onCreate được gọi
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideFolderDao(db: AppDatabase): FolderDao = db.folderDao()

    @Provides
    fun provideDeckDao(db: AppDatabase): DeckDao = db.deckDao()

    @Provides
    fun provideCardDao(db: AppDatabase): CardDao = db.cardDao()

    @Provides
    fun provideReviewDao(db: AppDatabase): ReviewDao = db.reviewDao()

    @Provides
    fun provideHistoryDao(db: AppDatabase): HistoryDao = db.historyDao()

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    @Singleton
    fun provideVocabularyRepository(
        folderDao: FolderDao,
        deckDao: DeckDao,
        cardDao: CardDao
    ): IVocabularyRepository {
        return VocabularyRepositoryImpl(folderDao, deckDao, cardDao)
    }

    @Provides
    @Singleton
    fun provideLearningRepository(
        reviewDao: ReviewDao,
        historyDao: HistoryDao
    ): ILearningRepository {
        return LearningRepositoryImpl(reviewDao, historyDao)
    }
}