package com.example.app_dich_quet_van_ban.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.app_dich_quet_van_ban.presentation.screens.* // Import tất cả màn hình
import com.example.app_dich_quet_van_ban.presentation.screens.Screens_Scan.CameraScreen
import com.example.app_dich_quet_van_ban.presentation.screens.Screens_Scan.ScanResultScreen
import com.example.app_dich_quet_van_ban.presentation.screens.Screens_Scan.ScanScreen
import com.example.app_dich_quet_van_ban.presentation.screens.Screens_Translate.TranslateScreen
import com.example.app_dich_quet_van_ban.presentation.screens.Screens_Vocabulary.LibraryScreen
import com.example.app_dich_quet_van_ban.presentation.screens.Screens_Vocabulary.AddFolderScreen
import com.example.app_dich_quet_van_ban.presentation.screens.Screens_Vocabulary.AddWordScreen
import com.example.app_dich_quet_van_ban.presentation.screens.Screens_Vocabulary.FolderDetailScreen
import com.example.app_dich_quet_van_ban.presentation.screens.Screens_Vocabulary.FlashcardLearningScreen
import com.example.app_dich_quet_van_ban.presentation.viewmodel.Viewmodel_Vocabulary.VocabularyViewModel


@OptIn(ExperimentalMaterial3Api::class)
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
        // Sử dụng ?docId={docId} để làm tham số tùy chọn
        composable(
            route = Screen.ScanResult.route + "/{scannedText}?docId={docId}",
            arguments = listOf(
                navArgument("scannedText") { type = NavType.StringType },
                navArgument("docId") {
                    type = NavType.IntType
                    defaultValue = 0 // Nếu từ Camera sang thì ID = 0
                }
            )
        ) { backStackEntry ->
            val rawText = backStackEntry.arguments?.getString("scannedText") ?: ""
            val docId = backStackEntry.arguments?.getInt("docId") ?: 0

            val decodedText = try {
                java.net.URLDecoder.decode(rawText, "UTF-8")
            } catch (e: Exception) {
                rawText
            }

            ScanResultScreen(
                scannedText = decodedText,
                docId = docId, // TRUYỀN ID VÀO ĐÂY
                onNavigateBack = { navController.popBackStack() },
//                onSaveComplete = { name, content, type ->
//                    // Bạn có thể giữ hoặc xóa lambda này tùy vào việc
//                    // bạn đã xử lý lưu trong ViewModel của ScanResultScreen chưa.
//                }
            )
        }

        composable(Screen.Practice.route) { PracticeScreen() }
//        composable(Screen.Vocabulary.route) { VocabularyScreen() }

        // =========================================================
        // --- VOCABULARY FLOW (đặt trực tiếp trong NavHost chính) ---
        // =========================================================

        // 6a. Library (danh sách folder)
        composable(Screen.Library.route) {
            LibraryScreen(
                onNavigateToDetail = { folderId ->
                    navController.navigate(Screen.FolderDetail.passFolderId(folderId))
                },
                onNavigateToAddFolder = {
                    navController.navigate(Screen.AddFolder.route)
                }
            )
        }

        // 6b. Add Folder
        composable(Screen.AddFolder.route) {
            AddFolderScreen(
                onBack = { navController.popBackStack() },
                onFolderCreated = { navController.popBackStack() }
            )
        }

        // 6c. Folder Detail
        composable(
            route = Screen.FolderDetail.route,
            arguments = listOf(navArgument("folderId") { type = NavType.IntType })
        ) { entry ->
            val folderId = entry.arguments?.getInt("folderId") ?: 0
            FolderDetailScreen(
                folderId = folderId,
                onBack = { navController.popBackStack() },
                onAddWord = {
                    navController.navigate(Screen.AddWord.passFolderId(folderId))
                },
                onStartSession = {
                    navController.navigate(Screen.FlashcardLearn.passFolderId(folderId))
                }
            )
        }

        // 6d. Add Word
        composable(
            route = Screen.AddWord.route,
            arguments = listOf(navArgument("folderId") { type = NavType.IntType })
        ) { entry ->
            val folderId = entry.arguments?.getInt("folderId") ?: 0
            val viewModel: VocabularyViewModel = hiltViewModel()
            AddWordScreen(
                folderId = folderId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // 6e. Flashcard Learning
        composable(
            route = Screen.FlashcardLearn.route,
            arguments = listOf(navArgument("folderId") { type = NavType.IntType })
        ) { entry ->
            val folderId = entry.arguments?.getInt("folderId") ?: 0
            val viewModel: VocabularyViewModel = hiltViewModel()
            FlashcardLearningScreen(
                folderId = folderId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

    }
}