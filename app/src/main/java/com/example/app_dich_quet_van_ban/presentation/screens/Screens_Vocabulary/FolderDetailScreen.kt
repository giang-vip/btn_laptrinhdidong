package com.example.app_dich_quet_van_ban.presentation.screens.Screens_Vocabulary

import android.R.attr.onClick
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.app_dich_quet_van_ban.data.local.entity.CardEntity
import com.example.app_dich_quet_van_ban.data.mock.MockData
import com.example.app_dich_quet_van_ban.presentation.viewmodel.Viewmodel_Vocabulary.VocabularyViewModel
import java.util.Locale

private val DetailNavyBlue = Color(0xFF1A3A5C)
private val DetailBgGray = Color(0xFFF0F4F8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderDetailScreen(
    folderId: Int,
    viewModel: VocabularyViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onAddWord: () -> Unit,
    onStartSession: (Int) -> Unit
) {

    // 1. Lấy thông tin Folder hiện tại từ ViewModel
    val context = LocalContext.current
    val folders by viewModel.folders.collectAsState()
    val folder = folders.find { it.folderId == folderId }
    val cards by viewModel.getCardsInFolder(folderId).collectAsState(initial = emptyList())

    // --- Khởi tạo bộ phát âm TextToSpeech ---
    val tts = remember { mutableStateOf<TextToSpeech?>(null) }
    DisposableEffect(context) {
        val speech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.value?.language = Locale.US
            }
        }
        tts.value = speech
        onDispose {
            speech.stop()
            speech.shutdown()
        }
    }
    Scaffold(
        containerColor = DetailBgGray,
        topBar = {
            TopAppBar(
                title = { Text(folder?.folderName ?: "Collection", fontWeight = FontWeight.Bold, color = DetailNavyBlue, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DetailNavyBlue)
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray) }
                    IconButton(onClick = {}) { Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color.Gray) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DetailBgGray)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
        ) {
            item {
                // ✅ FIX: Bỏ Alignment.Baseline, dùng verticalAlignment = Alignment.Bottom trong Row thường
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Card count header — hai Text trong Row, align Bottom
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            "${cards.size}",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF1A1A2E)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Mastery Cards",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DetailNavyBlue,
                            modifier = Modifier.padding(bottom = 2.dp) // căn chỉnh thủ công thay Baseline
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(folder?.description ?: "Your vocabulary collection", fontSize = 13.sp, color = Color.Gray)
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { onStartSession(0) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DetailNavyBlue)
                ) {
                    Text("▶", fontSize = 14.sp)
                    Spacer(Modifier.width(8.dp))
                    Text("Start Session", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Spacer(Modifier.height(20.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("CURATED VOCABULARY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray, letterSpacing = 1.sp)
                    Button(onClick = onAddWord) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Text("Add Word")
                    }
                }
                Spacer(Modifier.height(12.dp))
            }

            // Dùng itemsIndexed để truyền Index khi click vào từ bất kỳ
            // Sử dụng itemsIndexed để lấy vị trí index của từng từ
            itemsIndexed(cards) { index, card ->
                DetailWordCard(
                    card = card,
                    onDelete = { viewModel.deleteCard(card) },
                    // Khi click vào cả cái thẻ, chuyển đến màn hình học tại vị trí index
                    onCardClick = { onStartSession(index) },
                    // Khi click vào nút loa, phát âm từ đó
                    onSpeak = { tts.value?.speak(card.word, TextToSpeech.QUEUE_FLUSH, null, null) }
                )
                Spacer(Modifier.height(12.dp))
            }


            if (cards.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("📚", fontSize = 40.sp)
                            Spacer(Modifier.height(12.dp))
                            Text("No words yet", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Gray)
                            Text(
                                "Tap \"Add Word\" to start building your collection",
                                fontSize = 13.sp, color = Color.LightGray, textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailWordCard(
    card: CardEntity,
    onDelete: () -> Unit,
    onCardClick: () -> Unit,
    onSpeak: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() } // Sửa lỗi: Gọi đúng hàm callback khi click
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(onClick = onSpeak,
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFE8EEF4),
                    modifier = Modifier.size(36.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.VolumeUp, contentDescription = null, tint = DetailNavyBlue, modifier = Modifier.size(18.dp))
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(card.word, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1A1A2E))
                    Text(card.meaning, fontSize = 13.sp, color = Color.Gray)
                }
                IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.LightGray)
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.LightGray)
                }
            }

            card.exampleSentence?.let { sentence ->
                if (sentence.isNotBlank()) {
                    Spacer(Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF8F8F8), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Box(modifier = Modifier.width(2.dp).height(36.dp).background(DetailNavyBlue.copy(alpha = 0.3f), RoundedCornerShape(1.dp)))
                        Spacer(Modifier.width(10.dp))
                        Text("\"$sentence\"", fontSize = 12.sp, fontStyle = FontStyle.Italic, color = Color.Gray, lineHeight = 18.sp)
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            val isMastered = (card.cardId % 2 == 0)
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isMastered) Color(0xFFDFF5EA) else Color(0xFFE8EEF4)
            ) {
                Text(
                    if (isMastered) "MASTERED" else "LEARNING",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                    color = if (isMastered) Color(0xFF2E7D5B) else Color(0xFF5A7A9B)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FolderDetailScreenPreview() {
    MaterialTheme {
        FolderDetailScreen(folderId = 1, onBack = {}, onAddWord = {}, onStartSession = {})
    }
}