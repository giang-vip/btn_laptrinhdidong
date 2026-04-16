package com.example.app_dich_quet_van_ban.presentation.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_dich_quet_van_ban.data.local.AppDatabase
import com.example.app_dich_quet_van_ban.data.local.entity.ScannedDocEntity
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import android.util.Log

class ScanViewModel(application: Application) : AndroidViewModel(application) {
    private val scanDao = AppDatabase.getDatabase(application).scanDao()

    // Chuyển Flow thành StateFlow để UI dễ quan sát hơn trong Compose
    val allDocs: StateFlow<List<ScannedDocEntity>> = scanDao.getAllScannedDocs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    // Sửa tham số: Thêm 'content' và 'filePath' để lấy dữ liệu thực tế
    fun addDocument(name: String, filePath: String, content: String) {
        viewModelScope.launch {
            // Lấy 20 ký tự đầu làm tên nếu tên trống
            val finalName = if (name.isBlank()) {
                if (content.length > 20) content.take(20).replace("\n", " ") + "..." else content
            } else name

            val newDoc = ScannedDocEntity(
                fileName = finalName,
                fileSize = "${content.length / 1024} KB",
                createdAt = System.currentTimeMillis(),
                fileType = "TEXT",
                filePath = filePath,// Sau này làm logic xuất file thật sẽ điền đường dẫn vào đây
                content = content// Lưu nội dung quét được vào DB
            )
            scanDao.insertDoc(newDoc)
        }
    }

    fun updateDocument(doc: ScannedDocEntity) {
        viewModelScope.launch {
            scanDao.updateDoc(doc) // Sử dụng lệnh Update của Room thay vì Insert
        }
    }
    fun deleteDocument(doc: ScannedDocEntity) {
        viewModelScope.launch {
            scanDao.deleteDoc(doc)
        }
    }


}


