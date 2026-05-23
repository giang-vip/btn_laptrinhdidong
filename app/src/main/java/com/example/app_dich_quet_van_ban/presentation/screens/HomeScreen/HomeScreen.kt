package com.example.app_dich_quet_van_ban.presentation.screens.HomeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_dich_quet_van_ban.presentation.theme.*

@Composable
fun HomeScreen(
    onNavigateToTranslate: () -> Unit,
    onNavigateToScan: () -> Unit,
    onNavigateToVocabulary: () -> Unit, // Đã thêm đường dẫn học từ vựng
    onNavigateToChatAi: () -> Unit       // Đã thêm đường dẫn chat AI
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        // --- PHẦN CHÀO MỪNG ---
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Xin chào! 👋",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Hôm nay bạn muốn học gì nào?",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- HỆ THỐNG LƯỚI TÍNH NĂNG (GRID 2 CỘT) ---
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // 1. DỊCH VĂN BẢN
            item {
                FeatureGridCard(
                    title = "Dịch văn bản",
                    description = "Nhập hoặc dán chữ từ clipboard",
                    icon = Icons.Default.Translate,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    onClick = onNavigateToTranslate
                )
            }

            // 2. QUÉT TÀI LIỆU (OCR)
            item {
                FeatureGridCard(
                    title = "Quét tài liệu",
                    description = "Chụp hoặc chọn hình trích chữ",
                    icon = Icons.Default.CameraAlt,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    onClick = onNavigateToScan
                )
            }

            // 3. LUYỆN TỪ VỰNG (Tính năng mới)
            item {
                FeatureGridCard(
                    title = "Học từ vựng",
                    description = "Ôn tập flashcard và thư mục từ",
                    icon = Icons.Default.School,
                    // Dùng màu sắc thái tím/hồng ấm áp đại diện cho giáo dục
                    containerColor = Color(0xFFF3E5F5),
                    contentColor = Color(0xFF9C27B0),
                    onClick = onNavigateToVocabulary
                )
            }

            // 4. TRỢ LÝ CHAT AI (Tính năng mới)
            item {
                FeatureGridCard(
                    title = "Trợ lý Chat AI",
                    description = "Hỏi đáp, luyện hội thoại thông minh",
                    icon = Icons.Default.AutoAwesome,
                    // Dùng màu sắc thái xanh mint/teal đại diện cho trí tuệ nhân tạo thông minh
                    containerColor = Color(0xFFE0F2F1),
                    contentColor = Color(0xFF00796B),
                    onClick = onNavigateToChatAi
                )
            }
        }
    }
}

@Composable
fun FeatureGridCard(
    title: String,
    description: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp) // Định hướng chiều cao cố định cho dạng Grid vuông vắn
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween // Đẩy Icon lên trên và Text xuống dưới
        ) {
            // Phần Icon bọc trong khung tròn tinh tế
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(16.dp),
                color = contentColor.copy(alpha = 0.12f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = contentColor
                    )
                }
            }

            // Khối nội dung chữ thông tin tính năng
            Column {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = contentColor,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = contentColor.copy(alpha = 0.65f),
                    lineHeight = 16.sp,
                    maxLines = 2
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    AppDichQuetVanBanTheme {
        HomeScreen(
            onNavigateToTranslate = {},
            onNavigateToScan = {},
            onNavigateToVocabulary = {},
            onNavigateToChatAi = {}
        )
    }
}