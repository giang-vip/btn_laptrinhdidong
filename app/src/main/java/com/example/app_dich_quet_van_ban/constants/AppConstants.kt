package com.example.app_dich_quet_van_ban.utils

object AppConfig {
    // 1. Cấu hình Server Flask (Local)
    // Dùng 10.0.2.2 cho máy ảo, IP máy tính cho máy thật
    private const val BASE_URL_LOCAL = "http://192.168.200.5:5000"
    const val SUMMARIZE_URL = "$BASE_URL_LOCAL/summarize"
    const val CHAT_URL = "$BASE_URL_LOCAL/chat"

    // 2. Cấu hình API bên ngoài (Nếu vẫn dùng)
    const val GROQ_API_KEY = "gsk_xxxx..." // Để ở đây vẫn lộ nếu push lên Git, nhưng dễ quản lý hơn
    const val GROQ_URL = "https://api.groq.com/openai/v1/chat/completions"

    // 3. Tên Model
    const val MODEL_LLAMA_LOCAL = "llama3.1"
    const val MODEL_GROQ = "llama3-8b-8192"
}