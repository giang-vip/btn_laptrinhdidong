package com.example.app_dich_quet_van_ban.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_dich_quet_van_ban.data.remote.ChatApiService
import com.example.app_dich_quet_van_ban.data.remote.ChatRequest
import com.example.app_dich_quet_van_ban.presentation.screens.ChatMessage
import com.example.app_dich_quet_van_ban.presentation.screens.MessageSender
import com.example.app_dich_quet_van_ban.utils.AppConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class ChatAiViewModel : ViewModel() {

    // Danh sách tin nhắn động kết nối trực tiếp với giao diện
    val messages = mutableStateListOf<ChatMessage>()

    // Trạng thái Loading để hiển thị hiệu ứng đợi AI phản hồi
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // Khởi tạo bộ đếm thời gian OkHttpClient nới lỏng thời gian chờ lên 90s tránh lỗi Timeout
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .build()
    }

    // Tự khởi tạo Retrofit kết nối tới IP LAN công khai của AppConfig
    private val apiService: ChatApiService by lazy {
        Retrofit.Builder()
            .baseUrl(AppConfig.BASE_URL_LOCAL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChatApiService::class.java)
    }

    init {
        // Lời chào khởi tạo màn hình chat ban đầu
        if (messages.isEmpty()) {
            messages.add(
                ChatMessage(
                    id = "welcome_msg",
                    text = "Xin chào! Tôi là Trợ lý AI Ngôn Ngữ (Llama 3.1). Bạn cần tôi dịch thuật hay tóm tắt tài liệu gì không?",
                    sender = MessageSender.AI,
                    timestamp = getCurrentTime()
                )
            )
        }
    }

    // Hàm xử lý gửi tin nhắn của User và lấy dữ liệu từ Ollama về
    fun sendMessage(text: String, onSendExecuted: () -> Unit) {
        if (text.isBlank()) return

        val userTime = getCurrentTime()
        val userMessage = ChatMessage(
            id = System.currentTimeMillis().toString(),
            text = text.trim(),
            sender = MessageSender.USER,
            timestamp = userTime
        )

        // Thêm tin nhắn của User lên màn hình ngay lập tức
        messages.add(userMessage)
        onSendExecuted()

        // Bật luồng Coroutine để gọi API chạy ngầm (Dispatchers.IO)
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                // Gọi sang server Flask Python
                val response = apiService.sendChatQuery(ChatRequest(query = text.trim()))

                // Trở lại Main Thread để cập nhật giao diện hiển thị câu trả lời từ AI
                withContext(Dispatchers.Main) {
                    messages.add(
                        ChatMessage(
                            id = System.currentTimeMillis().toString(),
                            text = response.response,
                            sender = MessageSender.AI,
                            timestamp = getCurrentTime()
                        )
                    )
                }
            } catch (e: Exception) {
                // Hiển thị trực tiếp nội dung thông báo lỗi kết nối vào bong bóng chat
                withContext(Dispatchers.Main) {
                    messages.add(
                        ChatMessage(
                            id = System.currentTimeMillis().toString(),
                            text = "❌ Không thể kết nối với AI. Hãy kiểm tra xem Server Python đã chạy chưa nhé! Lỗi: ${e.localizedMessage}",
                            sender = MessageSender.AI,
                            timestamp = getCurrentTime()
                        )
                    )
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearChat() {
        messages.clear()
    }

    private fun getCurrentTime(): String {
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
    }
}