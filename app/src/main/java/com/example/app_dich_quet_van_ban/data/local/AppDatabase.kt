package com.example.app_dich_quet_van_ban.data.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.app_dich_quet_van_ban.data.local.dao.*
import com.example.app_dich_quet_van_ban.data.local.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.json.JSONArray

@Database(
    entities = [
        ScannedDocEntity::class,
        TranslationEntity::class,
        UserEntity::class,
        FolderEntity::class,
        DeckEntity::class,
        CardEntity::class,
        ReviewEntity::class,
        HistoryEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scanDao(): ScanDao
    abstract fun userDao(): UserDao
    abstract fun folderDao(): FolderDao
    abstract fun deckDao(): DeckDao
    abstract fun cardDao(): CardDao
    abstract fun reviewDao(): ReviewDao
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val dbScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Log.d("DB_PREPOPULATE", "Database onCreate triggered.")
                            // Sử dụng dbScope để chạy tiến trình nạp dữ liệu nền
                            INSTANCE?.let { database ->
                                dbScope.launch {
                                    prePopulateData(context.applicationContext, database)
                                }
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return getDatabase(context)
        }

        private suspend fun prePopulateData(context: Context, database: AppDatabase) {
            try {
                Log.d("DB_PREPOPULATE", "Bắt đầu nạp dữ liệu từ JSON...")

                // 1. Tạo sẵn User mặc định (userId = 1)
                val uId = database.userDao().insertUser(
                    UserEntity(userId = 1, username = "Admin", email = "admin@app.com")
                )
                Log.d("DB_PREPOPULATE", "Đã chèn User mặc định với ID: $uId")

                // Đọc file JSON từ thư mục assets
                val jsonString = context.assets.open("default_vocab.json").bufferedReader().use { it.readText() }
                val jsonArray = JSONArray(jsonString)

                for (i in 0 until jsonArray.length()) {
                    val folderObj = jsonArray.getJSONObject(i)

                    // 2. Chèn Folder
                    val folder = FolderEntity(
                        folderId = 0, // Room tự sinh ID
                        userId = 1,
                        folderName = folderObj.optString("folderName", "New Folder"),
                        description = folderObj.optString("description", ""),
                        colorHex = folderObj.optString("colorHex", "#673AB7"),
                        createdAt = System.currentTimeMillis()
                    )
                    val fId = database.folderDao().insert(folder)
                    Log.d("DB_PREPOPULATE", "Đã chèn Folder: ${folder.folderName} với ID: $fId")

                    // 3. Chèn Deck tương ứng với ID của Folder vừa tạo
                    val dId = database.deckDao().insertDeck(
                        DeckEntity(
                            deckId = 0,
                            folderId = fId.toInt(),
                            deckName = folder.folderName + " Deck",
                            createdAt = System.currentTimeMillis()
                        )
                    )
                    Log.d("DB_PREPOPULATE", "Đã chèn Deck tương ứng với ID: $dId")

                    // 4. Chèn danh sách các từ vựng (Cards)
                    val wordsArray = folderObj.getJSONArray("words")
                    for (j in 0 until wordsArray.length()) {
                        val wordObj = wordsArray.getJSONObject(j)
                        database.cardDao().insertCard(
                            CardEntity(
                                cardId = 0,
                                deckId = dId.toInt(),
                                word = wordObj.optString("word", ""),
                                meaning = wordObj.optString("meaning", ""),
                                pronunciation = null,
                                exampleSentence = wordObj.optString("example", ""),
                                imagePath = null,
                                mnemonicNote = null
                            )
                        )
                    }
                    Log.d("DB_PREPOPULATE", "Đã chèn ${wordsArray.length()} từ vào folder: ${folder.folderName}")
                }
                Log.d("DB_PREPOPULATE", "Hoàn thành nạp dữ liệu mẫu thành công!")
            } catch (e: Exception) {
                Log.e("DB_PREPOPULATE", "Lỗi trong quá trình nạp dữ liệu: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}