package com.example.app_dich_quet_van_ban.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.ui.graphics.vector.ImageVector

// Định nghĩa các màn hình và Icon tương ứng
sealed class Screen(
    val route: String,
    val label: String ="",
    val icon: ImageVector? = null
) {
    // --- NHÓM 1: CÁC MÀN HÌNH CHÍNH (XUẤT HIỆN Ở BOTTOM BAR) ---
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Translate : Screen("translate", "Dịch", Icons.Default.Language)
    object Scan : Screen("scan", "Quét", Icons.Default.QrCodeScanner)
    object Practice : Screen("practice", "Luyện nói", Icons.Default.Mic)
    object Library : Screen("vocabulary", "Từ vựng", Icons.Default.Book)

    // --- NHÓM 2: CÁC MÀN HÌNH PHỤ/CHI TIẾT (KHÔNG HIỆN Ở BOTTOM BAR) ---

    // luong màn hình quét van ban
    object Camera : Screen("camera", "Camera", Icons.Default.QrCodeScanner)
    object ScanResult : Screen("scan_result", "Kết quả quét", Icons.Default.Description)

    // Luồng Vocabulary mới
    object AddFolder : Screen("add_folder")

    // Màn hình chi tiết Folder (Cần folderId)
    object FolderDetail : Screen("folder_detail/{folderId}") {
        fun passFolderId(id: Int) = "folder_detail/$id"
    }

    // Màn hình học Flashcard (Cần folderId)
    object FlashcardLearn : Screen("flashcard_learn/{folderId}") {
        fun passFolderId(id: Int) = "flashcard_learn/$id"
    }

    // Màn hình thêm từ mới
    object AddWord : Screen("add_word/{folderId}") {
        fun passFolderId(id: Int) = "add_word/$id"
    }
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Translate,
    Screen.Scan,
    Screen.Practice,
    Screen.Library
)