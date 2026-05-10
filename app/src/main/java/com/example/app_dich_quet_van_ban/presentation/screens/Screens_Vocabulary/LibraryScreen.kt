package com.example.app_dich_quet_van_ban.presentation.screens.Screens_Vocabulary

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.app_dich_quet_van_ban.data.local.entity.FolderEntity
import com.example.app_dich_quet_van_ban.presentation.viewmodel.Viewmodel_Vocabulary.VocabularyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToAddFolder: () -> Unit,
    viewModel: VocabularyViewModel = hiltViewModel()
) {
    // LẤY DỮ LIỆU THẬT từ Database qua ViewModel
    val folders by viewModel.folders.collectAsState(initial = emptyList())

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Thư viện từ vựng", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        // Sử dụng Grid 2 cột để giao diện cân đối, không chồng chéo
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header tiêu đề
            item(span = { GridItemSpan(2) }) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Bộ sưu tập", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                        Text("Phát triển vốn từ của bạn", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    IconButton(
                        onClick = onNavigateToAddFolder,
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }
            }

            // Danh sách Folder thật từ DB
            items(folders, key = { it.folderId }) { folder ->
                LibraryFolderCard(
                    folder = folder,
                    onOpen = { onNavigateToDetail(folder.folderId) }
                )
            }

            // Nút tạo mới nhanh
            item {
                LibraryStartFreshCard(onClick = onNavigateToAddFolder)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LibraryFolderCard(folder: FolderEntity, onOpen: () -> Unit) {
    val cardColor = remember(folder.colorHex) {
        try { Color(android.graphics.Color.parseColor(folder.colorHex)) }
        catch (e: Exception) { Color(0xFF673AB7) }
    }

    Card(
        onClick = onOpen,
        modifier = Modifier.fillMaxWidth().height(180.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(12.dp),
                color = cardColor.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Book, contentDescription = null, tint = cardColor, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(Modifier.weight(1f))
            Text(folder.folderName, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1)
            Text(folder.description ?: "Tăng cường trí nhớ", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
            Spacer(Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { 0.4f },
                modifier = Modifier.fillMaxWidth().height(4.dp),
                color = cardColor,
                trackColor = cardColor.copy(alpha = 0.1f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LibraryStartFreshCard(onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(180.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Tạo bộ mới", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        }
    }
}

//@Preview
//@Composable
//fun SimpleComposablePreview() {
//    LibraryScreen()
//}