package com.example.app_dich_quet_van_ban.presentation.viewmodel

import TranslationRepositoryImpl
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.example.app_dich_quet_van_ban.data.local.AppDatabase
import com.example.app_dich_quet_van_ban.data.local.entity.ScannedDocEntity
import com.example.app_dich_quet_van_ban.utils.AppConfig
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.logging.Handler

class ScanResultViewModel(application: Application) : AndroidViewModel(application) {
    private val scanDao = AppDatabase.getDatabase(application).scanDao()


    // Biến để giữ ID của file hiện tại
    // Lưu ID dưới dạng Int (khớp với Entity)
    private var currentDocId: Int = 0

    fun saveDocument(name: String, content: String, type: String) {
        // Mọi thao tác Database PHẢI nằm trong launch
        viewModelScope.launch {
            try {
                val docEntity = ScannedDocEntity(
                    id = currentDocId, // Nếu 0 là insert, nếu > 0 là replace/update
                    fileName = name,
                    fileSize = "${content.length / 1024} KB",
                    createdAt = System.currentTimeMillis(),
                    fileType = type,
                    filePath = "",
                    content = content
                )

                // Lưu và lấy ID trả về
                val resultId = scanDao.insertDoc(docEntity)

                // Cập nhật lại currentDocId để lần nhấn nút sau sẽ là Update file này
                currentDocId = resultId.toInt()

                Log.d("DB_DEBUG", "Success! ID hiện tại là: $currentDocId")
            } catch (e: Exception) {
                Log.e("DB_ERROR", "Không thể lưu file: ${e.message}")
            }
        }
    }

    fun saveOrUpdateDocument(fileName: String, content: String, type: String) {
        viewModelScope.launch {
            // Gọi repository để tìm theo fileName
            val existingDoc =TranslationRepositoryImpl(scanDao).getDocByTitle(fileName)


            if (existingDoc != null) {
                // NẾU CÓ: Cập nhật nội dung (SỬA: timestamp -> createdAt)
                val updatedDoc = existingDoc.copy(
                    content = content,
                    fileType = type,
                    createdAt = System.currentTimeMillis()
                )
                TranslationRepositoryImpl(scanDao).insertDoc(updatedDoc)
            } else {
                // NẾU CHƯA CÓ: Tạo mới (SỬA: title -> fileName)
                val newDoc = ScannedDocEntity(
                    fileName = fileName,
                    content = content,
                    fileType = type,
                    createdAt = System.currentTimeMillis(),
                    fileSize = "${content.length / 1024} KB",
                    filePath = ""
                )
                TranslationRepositoryImpl(scanDao).insertDoc(newDoc)
            }
        }
    }

    fun summarizeText(text: String, onResult: (String) -> Unit) {
        if (text.isBlank()) return onResult("Văn bản rỗng")

        val client = OkHttpClient()

        // Gọi từ file cấu hình, không viết chết URL ở đây nữa
        val url = AppConfig.SUMMARIZE_URL

        val jsonBody = JSONObject().apply {
            put("text", text)
        }

        val request = Request.Builder()
            .url(url)
            .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
            .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // In lỗi ra Logcat để kiểm tra trên máy tính
                    android.util.Log.e("FLASK_ERROR", "--- LỖI KẾT NỐI API LOCAL ---")
                    e.printStackTrace()

                    android.os.Handler(android.os.Looper.getMainLooper()).post {
                        onResult("Lỗi kết nối máy chủ: ${e.message}")
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body?.string()
                    // In log phản hồi từ Flask ra Console máy tính
                    android.util.Log.d("FLASK_DEBUG", "Status: ${response.code}, Body: $responseBody")

                    if (response.isSuccessful && responseBody != null) {
                        try {
                            val jsonResponse = JSONObject(responseBody)
                            // Lấy giá trị từ key 'summary' mà Flask trả về
                            val result = jsonResponse.getString("summary")

                            android.os.Handler(android.os.Looper.getMainLooper()).post {
                                onResult(result.trim())
                            }
                        } catch (e: Exception) {
                            android.os.Handler(android.os.Looper.getMainLooper()).post {
                                onResult("Lỗi phân tích dữ liệu từ Python")
                            }
                        }
                    } else {
                        android.os.Handler(android.os.Looper.getMainLooper()).post {
                            onResult("Lỗi Server Flask (${response.code})")
                        }
                    }
                }
            })

    }
}