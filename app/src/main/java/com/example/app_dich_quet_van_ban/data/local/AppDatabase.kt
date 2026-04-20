package com.example.app_dich_quet_van_ban.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.app_dich_quet_van_ban.data.local.dao.*
import com.example.app_dich_quet_van_ban.data.local.entity.*

@Database(
    entities = [
        ScannedDocEntity::class, // Giữ nguyên bảng Quét văn bản cũ
        TranslationEntity::class,
        UserEntity::class,          // Bắt đầu các bảng Flashcard mới
        FolderEntity::class,
        DeckEntity::class,
        CardEntity::class,
        ReviewEntity::class,
        HistoryEntity::class
    ],
    version = 3, // TĂNG LÊN 2: Để Room biết có sự thay đổi cấu trúc
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // 1. DAO của tính năng Dịch & Quét cũ
    abstract fun scanDao(): ScanDao

    // 2. Các DAO của tính năng Flashcard mới (viết riêng rẽ)
    abstract fun userDao(): UserDao
    abstract fun folderDao(): FolderDao
    abstract fun deckDao(): DeckDao
    abstract fun cardDao(): CardDao
    abstract fun reviewDao(): ReviewDao
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    // Thêm dòng này để tránh lỗi khi nâng cấp version trong lúc đang phát triển
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}