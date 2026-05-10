package com.example.app_dich_quet_van_ban.presentation.screens.Screens_Vocabulary

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_dich_quet_van_ban.data.local.entity.CardEntity
import com.example.app_dich_quet_van_ban.data.mock.MockData
import com.example.app_dich_quet_van_ban.presentation.viewmodel.Viewmodel_Vocabulary.VocabularyViewModel

private val AddWordNavy = Color(0xFF1A3A5C)
private val AddWordBg = Color(0xFFF0F4F8)

data class StagingEntry(val term: String, val meaning: String, val scenario: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWordScreen(
    folderId: Int,
    viewModel: VocabularyViewModel,
    onBack: () -> Unit) {
    val context = LocalContext.current
    var term by remember { mutableStateOf("") }
    var meaning by remember { mutableStateOf("") }
    var scenario by remember { mutableStateOf("") }
    val stagingList = remember { mutableStateListOf<StagingEntry>() }

    // Trong AddWordScreen.kt
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // KHÔNG dùng .use { stream -> ... } ở đây nữa
            // Truyền trực tiếp uri cho ViewModel xử lý
            viewModel.importFromCSV(context, it, folderId)

            Toast.makeText(context, "Đang xử lý file...", Toast.LENGTH_SHORT).show()
        }
    }

    val folderName = MockData.folders.find { it.folderId == folderId }?.folderName ?: "FOLDER"

    Scaffold(
        containerColor = AddWordBg,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Expand Repository", fontWeight = FontWeight.Bold, color = AddWordNavy, fontSize = 18.sp)
                        Text("TARGET: ${folderName.uppercase()}", fontSize = 10.sp, color = Color.Gray, letterSpacing = 0.5.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = AddWordNavy)
                    }
                },
                actions = {
                    OutlinedButton(
                        onClick = {},
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, AddWordNavy),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text("Batch Actions", color = AddWordNavy, fontSize = 12.sp)
                    }
                    Spacer(Modifier.width(8.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AddWordBg)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // Header icon + title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFD6E4F7), modifier = Modifier.size(44.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("✦", fontSize = 20.sp, color = AddWordNavy)
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Manual Input", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color(0xFF1A1A2E))
                    Text("Add individual terms with precision.", fontSize = 12.sp, color = Color.Gray)
                }
            }

            Spacer(Modifier.height(20.dp))

            // Input form
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    AddWordFieldLabel(label = "LEXICAL TERM", icon = "#")
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value = term,
                        onValueChange = { term = it },
                        placeholder = { Text("e.g. Ubiquitous", color = Color.LightGray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = addWordFieldColors(),
                        singleLine = true
                    )

                    Spacer(Modifier.height(16.dp))

                    AddWordFieldLabel(label = "CONTEXTUAL MEANING", icon = "≡")
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value = meaning,
                        onValueChange = { meaning = it },
                        placeholder = { Text("Present, appearing, or found everywhere.", color = Color.LightGray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = addWordFieldColors(),
                        maxLines = 3
                    )

                    Spacer(Modifier.height(16.dp))

                    AddWordFieldLabel(label = "USAGE SCENARIO", icon = "\"")
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value = scenario,
                        onValueChange = { scenario = it },
                        placeholder = { Text("Mobile phones are ubiquitous in current society...", color = Color.LightGray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = addWordFieldColors(),
                        maxLines = 4
                    )

                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (term.isNotBlank() && meaning.isNotBlank()) {
                                stagingList.add(StagingEntry(term.trim(), meaning.trim(), scenario.trim()))
                                term = ""; meaning = ""; scenario = ""
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AddWordNavy),
                        enabled = term.isNotBlank() && meaning.isNotBlank()
                    ) {
                        Text("Append to List", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // External Import section
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.GridView, contentDescription = null, tint = AddWordNavy, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(10.dp))
                Text("External Import", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = Color(0xFF1A1A2E))
            }
            Spacer(Modifier.height(12.dp))
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        // Khi click sẽ mở trình chọn file hệ thống lọc file XLSX
                        filePickerLauncher.launch("text/*")
                    }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Color(0xFFF5F8FC), RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(shape = RoundedCornerShape(16.dp), color = Color.White, modifier = Modifier.size(56.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Upload, contentDescription = null, tint = AddWordNavy, modifier = Modifier.size(28.dp))
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        Text("Sync Excel/CSV", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1A1A2E))
                        Text("Drag assets to upload", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }

            // Staging Area (hiện khi có entry)
            if (stagingList.isNotEmpty()) {
                Spacer(Modifier.height(28.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row {
                            Text("Staging ", fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, color = Color(0xFF1A1A2E))
                            Text("Area", fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, color = AddWordNavy)
                        }
                        Text("Objects pending final archival.", fontSize = 12.sp, color = Color.Gray)
                    }
                    Button(
                        onClick = {
                            stagingList.forEach { entry ->
                                // Tạo thực thể CardEntity mới để chèn vào DB
                                viewModel.addCard(
                                    folderId = folderId,
                                    word = entry.term,
                                    meaning = entry.meaning,
                                    example = entry.scenario
                                )
                            }
                            stagingList.clear()
                            onBack() // Sau khi lưu xong thì quay lại màn hình chi tiết
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AddWordNavy),
                        shape = RoundedCornerShape(28.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Text("Commit All", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(Modifier.height(16.dp))

                stagingList.forEachIndexed { index, entry ->
                    AddWordStagingCard(index = index + 1, entry = entry, onDelete = { stagingList.removeAt(index) })
                    Spacer(Modifier.height(12.dp))
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}


@Composable
private fun AddWordFieldLabel(label: String, icon: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(icon, fontWeight = FontWeight.Bold, color = AddWordNavy, fontSize = 14.sp)
        Spacer(Modifier.width(6.dp))
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AddWordNavy, letterSpacing = 1.sp)
    }
}

@Composable
private fun addWordFieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedContainerColor = Color(0xFFF0F4F8),
    focusedContainerColor = Color(0xFFF0F4F8),
    unfocusedBorderColor = Color.Transparent,
    focusedBorderColor = AddWordNavy
)

@Composable
private fun AddWordStagingCard(index: Int, entry: StagingEntry, onDelete: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFE8EEF4)) {
                    Text("ENTRY $index", modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AddWordNavy, letterSpacing = 0.8.sp)
                }
                Row {
                    IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Red)
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(entry.term, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color(0xFF1A1A2E))
            Spacer(Modifier.height(4.dp))
            Text(entry.meaning, fontSize = 13.sp, color = Color.Gray)
            if (entry.scenario.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Text("ⓘ Analysis complete • Qualitya High", fontSize = 11.sp, color = AddWordNavy.copy(alpha = 0.6f))
            }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun AddWordScreenPreview() {
//    MaterialTheme { AddWordScreen(folderId = 1, onBack = {}) }
//}