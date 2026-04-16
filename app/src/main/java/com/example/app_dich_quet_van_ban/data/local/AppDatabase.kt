package com.example.app_dich_quet_van_ban.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.app_dich_quet_van_ban.data.local.dao.ScanDao
import com.example.app_dich_quet_van_ban.data.local.entity.ScannedDocEntity
@Database(entities = [ScannedDocEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scanDao(): ScanDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Hàm lấy Database (Singleton pattern - chỉ tạo 1 bản duy nhất để đỡ tốn RAM)
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database" // Tên file database lưu trên điện thoại
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}