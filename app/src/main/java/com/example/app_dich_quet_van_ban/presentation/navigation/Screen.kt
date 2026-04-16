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
sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Translate : Screen("translate", "Dịch", Icons.Default.Language)


    // luong màn hình quét van ban
    object Scan : Screen("scan", "Quét", Icons.Default.QrCodeScanner)
    object Camera : Screen("camera", "Camera", Icons.Default.QrCodeScanner)
    object ScanResult : Screen("scan_result", "Kết quả quét", Icons.Default.Description)




    object Practice : Screen("practice", "Luyện nói", Icons.Default.Mic)
    object Vocabulary : Screen("vocabulary", "Từ vựng", Icons.Default.Book)
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Translate,
    Screen.Scan,
    Screen.Practice,
    Screen.Vocabulary
)