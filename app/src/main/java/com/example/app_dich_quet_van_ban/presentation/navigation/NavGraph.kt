package com.example.app_dich_quet_van_ban.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.app_dich_quet_van_ban.presentation.screens.* // Import tất cả màn hình
import com.example.app_dich_quet_van_ban.presentation.viewmodel.ScanViewModel

@Composable
fun NavGraph(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        // --- 1. MÀN HÌNH CHÍNH (HOME) ---
        composable(Screen.Home.route) {
            // Sửa lại đoạn này để khớp với HomeScreen
            HomeScreen(
                onNavigateToTranslate = { navController.navigate(Screen.Translate.route) },
                onNavigateToScan = { navController.navigate(Screen.Scan.route) }
            )
        }

        // --- 2. MÀN HÌNH DỊCH VĂN BẢN ---
        composable(Screen.Translate.route) { TranslateScreen() }

        // --- 3. MÀN HÌNH DANH SÁCH CÁC FILE ĐÃ QUÉT ---
        composable(Screen.Scan.route) {
            // Cập nhật ScanScreen để có thể gọi lệnh chuyển sang Camera
            ScanScreen(
                navController = navController, // Truyền navController để bấm "View" có thể chuyển màn hình
                onNavigateToCamera = { navController.navigate(Screen.Camera.route) } // Bấm nút "Scan" thì đi đến Camera
            )
        }

        // --- 4. MÀN HÌNH CAMERA (MÁY ẢNH) ---
        composable(Screen.Camera.route) {
            CameraScreen(
                onTextScanned = { resultText ->
                    // KHI QUÉT XONG: Không dùng popBackStack nữa, mà nhảy thẳng sang màn hình Xem kết quả
                    // Truyền kèm theo đoạn văn bản quét được (resultText)
                    navController.navigate(Screen.ScanResult.route + "/$resultText") {
                        // Xóa màn hình Camera khỏi lịch sử để khi bấm nút quay lại nó không hiện camera đen thui nữa
                        popUpTo(Screen.Scan.route)
                    }
                },
                onNavigateBack = {
                    // Khi bấm nút mũi tên ở Camera, quay về màn hình danh sách quét
                    navController.popBackStack()
                }
            )
        }

        // --- 5. MÀN HÌNH XEM KẾT QUẢ & LƯU FILE ---
        composable(Screen.ScanResult.route + "/{scannedText}") { backStackEntry ->
            // 1. Lấy chuỗi đã bị mã hóa từ URL
            val rawText = backStackEntry.arguments?.getString("scannedText") ?: ""

            // 2. GIẢI MÃ nó ra lại thành văn bản bình thường (có dấu cách, xuống dòng)
            val decodedText = try {
                java.net.URLDecoder.decode(rawText, "UTF-8")
            } catch (e: Exception) {
                rawText // Nếu lỗi thì lấy bản gốc
            }

            val viewModel: ScanViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

            ScanResultScreen(
                scannedText = decodedText, // Truyền văn bản ĐÃ GIẢI MÃ vào đây
                onNavigateBack = { navController.popBackStack() },
                onSaveComplete = { name, content, type ->
                    viewModel.addDocument(name, type, content)
                    navController.navigate(Screen.Scan.route) {
                        popUpTo(Screen.Scan.route) { inclusive = true }
                    }
                }
            )
        }


        composable(Screen.Practice.route) { PracticeScreen() }
        composable(Screen.Vocabulary.route) { VocabularyScreen() }
    }
}