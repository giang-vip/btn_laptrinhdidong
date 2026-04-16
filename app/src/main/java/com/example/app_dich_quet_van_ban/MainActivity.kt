package com.example.app_dich_quet_van_ban

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import com.example.app_dich_quet_van_ban.presentation.components.BottomNavigationBar
import com.example.app_dich_quet_van_ban.presentation.navigation.NavGraph
import com.example.app_dich_quet_van_ban.presentation.theme.AppDichQuetVanBanTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            AppDichQuetVanBanTheme {
                // Khởi tạo bộ điều khiển chuyển màn hình
                val navController = rememberNavController()

                // Thay "AppTheme" bằng tên theme mặc định trong project của bạn
                // (Thường là: TênProjectTheme)
                MaterialTheme {
                    // Trong MainActivity.kt
                    Scaffold(// Gọi trực tiếp tên hàm, không cần thông qua Class nào cả
                        containerColor = MaterialTheme.colorScheme.background,
                        bottomBar = { BottomNavigationBar(navController) }
                    ) { innerPadding ->
                        NavGraph(navController, innerPadding)
                    }
                }
            }
        }
    }
}