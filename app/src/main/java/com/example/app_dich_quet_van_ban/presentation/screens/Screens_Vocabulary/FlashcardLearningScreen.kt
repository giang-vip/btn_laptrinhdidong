package com.example.app_dich_quet_van_ban.presentation.screens.Screens_Vocabulary

import android.speech.tts.TextToSpeech
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_dich_quet_van_ban.presentation.viewmodel.Viewmodel_Vocabulary.VocabularyViewModel
import java.util.Locale

private val FlashNavyBlue = Color(0xFF1A3A5C)
private val FlashBgGray = Color(0xFFF0F4F8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardLearningScreen(
    folderId: Int,
    initialIndex: Int = 0,
    viewModel: VocabularyViewModel,
    onBack: () -> Unit
) {
    val cards by viewModel.getCardsInFolder(folderId).collectAsState(initial = emptyList())
    val context = LocalContext.current

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

    var currentIndex by remember { mutableStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    LaunchedEffect(initialIndex) {
        currentIndex = initialIndex
    }

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "card_flip"
    )

    val folders by viewModel.folders.collectAsState()
    val folderName = folders.find { it.folderId == folderId }?.folderName ?: "Session"

    if (cards.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("📚", fontSize = 40.sp)
                Spacer(Modifier.height(12.dp))
                Text("No cards to study", fontWeight = FontWeight.Bold, color = Color.Gray)
                Button(onClick = onBack, modifier = Modifier.padding(top = 16.dp)) {
                    Text("Go Back")
                }
            }
        }
        return
    }

    val card = cards.getOrNull(currentIndex) ?: cards[0]

    Scaffold(
        containerColor = FlashBgGray,
        topBar = {
            TopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Book, null, tint = FlashNavyBlue, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Study Mode", fontWeight = FontWeight.Bold, color = FlashNavyBlue, fontSize = 16.sp)
                        }
                        Text(folderName.uppercase(), fontSize = 10.sp, color = Color.Gray, letterSpacing = 0.5.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = FlashNavyBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FlashBgGray)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("FOCUS PROGRESS", fontSize = 10.sp, color = Color.Gray, letterSpacing = 1.sp)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("${currentIndex + 1}", fontSize = 40.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1A1A2E))
                        Text(" / ${cards.size}", fontSize = 18.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 4.dp))
                    }
                }
                Spacer(Modifier.weight(1f))
                Column(horizontalAlignment = Alignment.End) {
                    LinearProgressIndicator(
                        progress = { (currentIndex + 1).toFloat() / cards.size },
                        modifier = Modifier.width(180.dp).height(6.dp),
                        color = FlashNavyBlue,
                        trackColor = Color(0xFFDDE4ED)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text("Stay focused", fontSize = 11.sp, color = Color.Gray, fontStyle = FontStyle.Italic)
                }
            }

            Spacer(Modifier.height(20.dp))

            Box(
                modifier = Modifier.fillMaxWidth().weight(1f)
                    .graphicsLayer { rotationY = rotation; cameraDistance = 12f * density }
                    .clickable { isFlipped = !isFlipped }
            ) {
                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Nội dung thẻ
                        Column(
                            modifier = Modifier.fillMaxSize().padding(28.dp).graphicsLayer {
                                // Nếu đang ở mặt sau thì xoay ngược lại 180 độ để chữ không bị ngược
                                if (rotation > 90f) rotationY = 180f
                            },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // --- Hàng tiêu đề (Dùng chung cho cả 2 mặt) ---
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Surface(shape = RoundedCornerShape(20.dp), color = Color(0xFFE8EEF4)) {
                                    Text(
                                        if (rotation <= 90f) "EXPRESSION" else "MEANING",
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        fontSize = 11.sp, fontWeight = FontWeight.Bold, color = FlashNavyBlue
                                    )
                                }
                                Surface(
                                    onClick = { tts.value?.speak(card.word, TextToSpeech.QUEUE_FLUSH, null, null) },
                                    shape = CircleShape, color = Color(0xFFF0F4F8), modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.VolumeUp, null, tint = FlashNavyBlue, modifier = Modifier.size(20.dp))
                                    }
                                }
                            }

                            // --- Nội dung chính ở giữa ---
                            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                if (rotation <= 90f) {
                                    // MẶT TRƯỚC
                                    Text(card.word, fontSize = 42.sp, fontWeight = FontWeight.ExtraBold, color = FlashNavyBlue, textAlign = TextAlign.Center)
                                } else {
                                    // MẶT SAU
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(card.meaning, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = FlashNavyBlue, textAlign = TextAlign.Center)
                                        if (!card.exampleSentence.isNullOrBlank()) {
                                            Spacer(Modifier.height(16.dp))
                                            Text("\"${card.exampleSentence}\"", fontSize = 15.sp, fontStyle = FontStyle.Italic, color = Color.Gray, textAlign = TextAlign.Center, lineHeight = 22.sp)
                                        }
                                    }
                                }
                            }

                            // --- Chân trang (Footer) ---
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(if (rotation <= 90f) "INTERROGATE CARD" else "RETAIN MEANING", fontSize = 10.sp, color = Color.LightGray, letterSpacing = 1.sp)
                                Spacer(Modifier.height(4.dp))
                                Box(modifier = Modifier.size(6.dp).background(FlashNavyBlue, CircleShape))
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Nút bấm điều hướng
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    onClick = { isFlipped = false },
                    shape = CircleShape, color = Color(0xFFE8EEF4), modifier = Modifier.size(52.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Refresh, null, tint = Color.Gray, modifier = Modifier.size(22.dp))
                    }
                }

                Button(
                    onClick = {
                        if (currentIndex < cards.size - 1) {
                            currentIndex++
                            isFlipped = false
                        } else {
                            onBack()
                        }
                    },
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1A2E))
                ) {
                    Text("Retain & Next", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(Modifier.width(8.dp))
                    Text("›", fontSize = 20.sp)
                }
            }
        }
    }
}