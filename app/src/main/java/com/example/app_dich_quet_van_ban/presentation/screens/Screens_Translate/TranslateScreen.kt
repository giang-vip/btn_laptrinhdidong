package com.example.app_dich_quet_van_ban.presentation.screens.Screens_Translate

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_dich_quet_van_ban.presentation.theme.*
import com.example.app_dich_quet_van_ban.presentation.viewmodel.Viewmodel_Translate.TranslationViewModel
import java.util.Locale

@Composable
fun TranslateScreen(
    viewModel: TranslationViewModel = viewModel()
) {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current

    val speechLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            viewModel.inputText = data?.get(0) ?: ""
        }
    }

    val historyItems = remember { listOf("Where is the nearest cafe?", "How are you?", "HCI Project is great!") }

    LaunchedEffect(Unit) {
        viewModel.prepareTranslator()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // --- 1. THANH CHỌN NGÔN NGỮ ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box {
                    TextButton(onClick = { viewModel.showSourceMenu = true }) {
                        Text(viewModel.sourceLangName, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        Icon(Icons.Default.ArrowDropDown, null, tint = MaterialTheme.colorScheme.primary)
                    }
                    DropdownMenu(
                        expanded = viewModel.showSourceMenu,
                        onDismissRequest = { viewModel.showSourceMenu = false }
                    ) {
                        viewModel.availableLanguages.forEach { lang ->
                            DropdownMenuItem(
                                text = { Text(lang.name) },
                                onClick = {
                                    viewModel.onSourceLangSelected(lang)
                                    viewModel.showSourceMenu = false
                                }
                            )
                        }
                    }
                }

                FilledIconButton(
                    onClick = { viewModel.swapLanguages() },
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.SwapHoriz, null, tint = MaterialTheme.colorScheme.onPrimary)
                }

                Box {
                    TextButton(onClick = { viewModel.showTargetMenu = true }) {
                        Text(viewModel.targetLangName, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        Icon(Icons.Default.ArrowDropDown, null, tint = MaterialTheme.colorScheme.primary)
                    }
                    DropdownMenu(
                        expanded = viewModel.showTargetMenu,
                        onDismissRequest = { viewModel.showTargetMenu = false }
                    ) {
                        viewModel.availableLanguages.forEach { lang ->
                            DropdownMenuItem(
                                text = { Text(lang.name) },
                                onClick = {
                                    viewModel.onTargetLangSelected(lang)
                                    viewModel.showTargetMenu = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- 2. Ô NHẬP VĂN BẢN ---
        Card(
            modifier = Modifier.fillMaxWidth().height(180.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                OutlinedTextField(
                    value = viewModel.inputText,
                    onValueChange = { viewModel.inputText = it },
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    placeholder = { 
                        Text(
                            "Nhập văn bản...", 
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        ) 
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                )

                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                            putExtra(RecognizerIntent.EXTRA_PROMPT, "Hãy nói gì đó...")
                        }
                        try {
                            speechLauncher.launch(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Thiết bị không hỗ trợ giọng nói", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(Icons.Default.Mic, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = { viewModel.translate() },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        enabled = !viewModel.isTranslating
                    ) {
                        if (viewModel.isTranslating) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                        } else {
                            Text("Translate", color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- 3. Ô HIỂN THỊ KẾT QUẢ ---
        Card(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = viewModel.outputText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.weight(1f)
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = { /* Logic Copy */ }) {
                        Icon(Icons.Default.ContentCopy, null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- 4. PHẦN LỊCH SỬ ---
        Text("History",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface)

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize().padding(top = 8.dp)
        ) {
            items(historyItems) { item ->
                HistoryCard(item)
            }
        }
    }
}

@Composable
fun HistoryCard(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "TIẾNG ANH",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                )
                Text(
                    text = text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = { /* Bookmark */ }) {
                Icon(Icons.Default.StarBorder, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}

@Preview(showSystemUi = true, name = "Màn Hình Dịch Full")
@Composable
fun PreviewTranslateFull() {
    AppDichQuetVanBanTheme { TranslateScreen() }
}
