package com.example.app_dich_quet_van_ban.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
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
import com.example.app_dich_quet_van_ban.presentation.theme.*

@Composable
fun HomeScreen(
    onNavigateToTranslate: () -> Unit,
    onNavigateToScan: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        // --- PHẦN CHÀO MỪNG ---
        Text(
            text = "Xin chào! 👋",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface // #0F172A từ Design System
        )
        Text(
            text = "Hôm nay bạn muốn dịch gì nào?",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant // #475569 từ Design System
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- NÚT TÍNH NĂNG 1: DỊCH VĂN BẢN (PRIMARY) ---
        FeatureCard(
            title = "Dịch văn bản",
            description = "Nhập văn bản hoặc dán từ clipboard",
            icon = Icons.Default.Translate,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            onClick = onNavigateToTranslate
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- NÚT TÍNH NĂNG 2: QUÉT TÀI LIỆU (SECONDARY) ---
        FeatureCard(
            title = "Quét tài liệu",
            description = "Chụp ảnh hoặc chọn hình để trích xuất chữ",
            icon = Icons.Default.CameraAlt,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            onClick = onNavigateToScan
        )
    }
}

@Composable
fun FeatureCard(
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
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(0.dp) // Flat design chuyên nghiệp
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(14.dp),
                color = contentColor.copy(alpha = 0.12f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.padding(14.dp),
                    tint = contentColor
                )
            }

            Spacer(modifier = Modifier.width(20.dp))
            
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor.copy(alpha = 0.7f)
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
            onNavigateToScan = {}
        )
    }
}
