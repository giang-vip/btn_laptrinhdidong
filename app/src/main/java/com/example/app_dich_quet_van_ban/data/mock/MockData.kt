package com.example.app_dich_quet_van_ban.data.mock

import androidx.compose.runtime.mutableStateListOf
import com.example.app_dich_quet_van_ban.data.local.entity.*

object MockData {
    val user = UserEntity(userId = 1, username = "guest", email = "guest@gmail.com")

    val folders = mutableStateListOf(
        FolderEntity(1, 1, "English Basic", "Từ cơ bản hằng ngày", "#673AB7"), // Tím
        FolderEntity(2, 1, "IT Terms", "Từ chuyên ngành CNTT", "#00BFA5")  // Xanh Teal
    )

    val decks = mutableStateListOf(
        DeckEntity(1, 1, "Unit 1: Greeting"),
        DeckEntity(2, 2, "Jetpack Compose")
    )

    val cards = mutableStateListOf(
        CardEntity(1, 1, "Hello", "Xin chào", "/həˈləʊ/", "Hello John", null, null),
        CardEntity(2, 1, "Apple", "Quả táo", "/ˈæp.əl/", "I eat apple", null, null),
        CardEntity(3, 2, "Composable", "Thành phần UI", null, "UI is a function", null, null)
    )
}