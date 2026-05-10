package com.example.app_dich_quet_van_ban.presentation.screens.Screens_Vocabulary

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CreateNewFolder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.app_dich_quet_van_ban.data.local.entity.FolderEntity
import com.example.app_dich_quet_van_ban.data.mock.MockData
import com.example.app_dich_quet_van_ban.presentation.viewmodel.Viewmodel_Vocabulary.VocabularyViewModel

private val AddFolderNavy = Color(0xFF1A3A5C)
private val AddFolderBg = Color(0xFFF0F4F8)

private val accentColorOptions = listOf(
    Color(0xFFAEC6E8),
    Color(0xFF7ED49B),
    Color(0xFFF4A8B5),
    Color(0xFFFFD580),
    Color(0xFFB5A8F4)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFolderScreen(
    onBack: () -> Unit,
    onFolderCreated: () -> Unit,
    viewModel: VocabularyViewModel = hiltViewModel()
) {
    var folderName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(accentColorOptions[0]) }

    Scaffold(
        containerColor = AddFolderBg,
        topBar = {
            TopAppBar(
                title = { Text("New Collection", fontWeight = FontWeight.Bold, color = AddFolderNavy, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = AddFolderNavy)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AddFolderBg)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))

            Surface(shape = RoundedCornerShape(20.dp), color = Color(0xFFD6E4F7), modifier = Modifier.size(72.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Outlined.CreateNewFolder, contentDescription = null, tint = AddFolderNavy, modifier = Modifier.size(36.dp))
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("Design Your Folder", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1A1A2E))
            Spacer(Modifier.height(6.dp))
            Text(
                "First, give your new vocabulary collection a name and identity.",
                fontSize = 13.sp, color = Color.Gray, textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(28.dp))

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    // Folder Name
                    AddFolderFieldLabel(label = "FOLDER NAME", icon = "T")
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = folderName,
                        onValueChange = { folderName = it },
                        placeholder = { Text("e.g., Business English 2024", color = Color.LightGray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = addFolderFieldColors(),
                        singleLine = true
                    )

                    Spacer(Modifier.height(20.dp))

                    // Description
                    AddFolderFieldLabel(label = "DESCRIPTION (OPTIONAL)", icon = "≡")
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        placeholder = { Text("What will you learn in this folder?", color = Color.LightGray) },
                        modifier = Modifier.fillMaxWidth().height(110.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = addFolderFieldColors(),
                        maxLines = 4
                    )

                    Spacer(Modifier.height(20.dp))

                    // Accent Color
                    AddFolderFieldLabel(label = "ACCENT COLOR", icon = "⊙")
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        accentColorOptions.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .then(
                                        if (color == selectedColor)
                                            Modifier.border(2.5.dp, AddFolderNavy, CircleShape)
                                        else Modifier
                                    )
                                    .clickable { selectedColor = color }
                            )
                        }
                    }

                    Spacer(Modifier.height(28.dp))

                    Button(
                        onClick = {
                            if (folderName.isNotBlank()) {
                                // 2. Chuyển màu từ Color sang Hex String
                                val hexColor = String.format(
                                    "#%02X%02X%02X",
                                    (selectedColor.red * 255).toInt(),
                                    (selectedColor.green * 255).toInt(),
                                    (selectedColor.blue * 255).toInt()
                                )

                                // 3. GỌI VIEWMODEL ĐỂ LƯU VÀO DATABASE THẬT
                                viewModel.addFolder(
                                    name = folderName.trim(),
                                    description = description.trim().ifBlank { null },
                                    colorHex = hexColor
                                )

                                // 4. Quay lại trang chính
                                onFolderCreated()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AddFolderNavy,
                            disabledContainerColor = Color(0xFF8A9BB0)
                        ),
                        enabled = folderName.isNotBlank()
                    ) {
                        Text("Create Collection", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun AddFolderFieldLabel(label: String, icon: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(icon, fontWeight = FontWeight.Bold, color = AddFolderNavy, fontSize = 14.sp)
        Spacer(Modifier.width(6.dp))
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AddFolderNavy, letterSpacing = 1.sp)
    }
}

@Composable
private fun addFolderFieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedContainerColor = Color(0xFFF0F4F8),
    focusedContainerColor = Color(0xFFF0F4F8),
    unfocusedBorderColor = Color.Transparent,
    focusedBorderColor = AddFolderNavy
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddFolderScreenPreview() {
    MaterialTheme { AddFolderScreen({}, {}) }
}