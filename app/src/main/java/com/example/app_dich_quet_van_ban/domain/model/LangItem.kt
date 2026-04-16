package com.example.app_dich_quet_van_ban.data.model

// Chức năng: Lưu trữ tên hiển thị và mã code của ngôn ngữ để ML Kit hiểu
data class LangItem(
    val name: String, // Ví dụ: "English"
    val code: String  // Ví dụ: TranslateLanguage.ENGLISH
)