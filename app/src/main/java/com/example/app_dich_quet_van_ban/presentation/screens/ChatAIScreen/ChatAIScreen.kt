package com.example.app_dich_quet_van_ban.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_dich_quet_van_ban.presentation.viewmodel.ChatAiViewModel
import kotlinx.coroutines.launch

enum class MessageSender { USER, AI }

data class ChatMessage(
    val id: String,
    val text: String,
    val sender: MessageSender,
    val timestamp: String,
    val hasImage: Boolean = false
)

private val SecondaryLight = Color(0xFF9C27B0)
private val OnSecondaryLight = Color(0xFFFFFFFF)
private val SecondaryContainerLight = Color(0xFFF3E5F5)
private val BackgroundLight = Color(0xFFF8F9FA)
private val SurfaceLight = Color(0xFFFFFFFF)
private val OnSurfaceLight = Color(0xFF1F1F1F)
private val OnSurfaceVariantLight = Color(0xFF444746)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(
    // ĐÃ KẾT NỐI: Nhận trực tiếp ViewModel độc lập tại đây
    chatViewModel: ChatAiViewModel = viewModel()
) {
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var menuExpanded by remember { mutableStateOf(false) }

    // ĐÃ ĐỒNG BỘ: Lấy danh sách tin nhắn và trạng thái loading trực tiếp từ ViewModel
    val messages = chatViewModel.messages
    val isLoading by chatViewModel.isLoading.collectAsState()

    // Tự động cuộn xuống tin nhắn mới nhất mỗi khi danh sách tin nhắn thay đổi kích thước
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        containerColor = BackgroundLight,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(40.dp)) {
                            Surface(
                                shape = CircleShape,
                                color = SecondaryContainerLight,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("AI", color = SecondaryLight, fontWeight = FontWeight.Bold)
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(Color(0xFF4CAF50), CircleShape)
                                    .align(Alignment.BottomEnd)
                                    .offset(x = (2).dp, y = (2).dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text("Trợ lý AI Ngôn Ngữ", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = OnSurfaceLight)
                            Text("Đang trực tuyến", fontSize = 11.sp, color = Color(0xFF4CAF50))
                        }
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More", tint = OnSurfaceVariantLight)
                        }

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
                            modifier = Modifier.background(SurfaceLight)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Làm mới cuộc trò chuyện", color = OnSurfaceLight) },
                                leadingIcon = {
                                    Icon(Icons.Default.Refresh, contentDescription = null, tint = SecondaryLight)
                                },
                                onClick = {
                                    chatViewModel.clearChat() // Gọi hàm xóa sạch hội thoại trong ViewModel
                                    menuExpanded = false
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceLight),
                modifier = Modifier.background(SurfaceLight)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
            ) {
                items(messages, key = { it.id }) { message ->
                    BubbleChatItem(message = message)
                }

                // ĐÃ THÊM: Bong bóng Loading xoay tròn khi AI đang xử lý câu trả lời từ Flask
                if (isLoading) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Surface(
                                shape = RoundedCornerShape(18.dp, 18.dp, 18.dp, 2.dp),
                                color = Color(0xFFEFEFEF)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        color = SecondaryLight,
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("AI đang suy nghĩ...", fontSize = 14.sp, color = Color.Gray)
                                }
                            }
                        }
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 2.dp,
                color = SurfaceLight
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .navigationBarsPadding()
                        .imePadding(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* Logic chọn ảnh từ máy */ }) {
                        Icon(Icons.Default.Image, contentDescription = "Gửi ảnh", tint = SecondaryLight)
                    }

                    IconButton(onClick = { /* Logic ghi âm giọng nói */ }) {
                        Icon(Icons.Default.Mic, contentDescription = "Ghi âm", tint = SecondaryLight)
                    }

                    TextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("Nhập tin nhắn...", fontSize = 15.sp, color = OnSurfaceVariantLight) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = BackgroundLight,
                            unfocusedContainerColor = BackgroundLight,
                            disabledContainerColor = BackgroundLight,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(24.dp),
                        maxLines = 4
                    )

                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank() && !isLoading) {
                                // ĐÃ SỬA: Gọi hàm sendMessage chuẩn từ ViewModel để bắn API lên Python
                                chatViewModel.sendMessage(
                                    text = inputText,
                                    onSendExecuted = {
                                        inputText = "" // Xóa nội dung khung chat ngay lập tức
                                    }
                                )
                            }
                        },
                        enabled = inputText.isNotBlank() && !isLoading
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Gửi",
                            tint = if (inputText.isNotBlank() && !isLoading) SecondaryLight else OnSurfaceVariantLight.copy(alpha = 0.4f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BubbleChatItem(message: ChatMessage) {
    val isUser = message.sender == MessageSender.USER

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Surface(
                shape = when {
                    isUser -> RoundedCornerShape(18.dp, 18.dp, 2.dp, 18.dp)
                    else -> RoundedCornerShape(18.dp, 18.dp, 18.dp, 2.dp)
                },
                color = if (isUser) SecondaryLight else Color(0xFFEFEFEF),
                contentColor = if (isUser) OnSecondaryLight else OnSurfaceLight
            ) {
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                    Text(
                        text = message.text,
                        fontSize = 15.sp,
                        lineHeight = 20.sp
                    )
                }
            }

            Text(
                text = message.timestamp,
                fontSize = 10.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp, start = 4.dp, end = 4.dp)
            )
        }
    }
}

@Preview(
    name = "Màn hình Chat AI - Chế độ sáng",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PracticeScreenPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundLight
    ) {
        PracticeScreen()
    }
}