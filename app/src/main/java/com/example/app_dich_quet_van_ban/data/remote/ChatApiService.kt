package com.example.app_dich_quet_van_ban.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

// Data class đại diện cho JSON Request gửi lên Flask
data class ChatRequest(val query: String)

// Data class đại diện cho JSON Response nhận về từ Flask
data class ChatResponse(val response: String)

interface ChatApiService {
    @POST("/chat")
    suspend fun sendChatQuery(@Body request: ChatRequest): ChatResponse
}