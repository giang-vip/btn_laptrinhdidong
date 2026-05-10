package com.example.app_dich_quet_van_ban.presentation.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.app_dich_quet_van_ban.presentation.navigation.bottomNavItems

/**
 * Hàm này tạo ra thanh điều hướng dưới cùng của ứng dụng.
 * Chúng ta không để nó trong class nào cả để MainActivity có thể gọi trực tiếp.
 */
@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        // Theo dõi xem bạn đang đứng ở màn hình nào
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        // Chạy vòng lặp để tạo ra 5 nút bấm (Home, Translate, Scan, Practice, Vocab)
        bottomNavItems.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    // Logic chuyển màn hình
                    navController.navigate(screen.route) {
                        // Giúp khi bấm nút Back không bị quay lại các màn hình cũ lặp đi lặp lại
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = { Text(text = screen.label) },
                icon = {
                    Icon(
                        imageVector = screen.icon!!,
                        contentDescription = screen.label
                    )
                }
            )
        }
    }
}