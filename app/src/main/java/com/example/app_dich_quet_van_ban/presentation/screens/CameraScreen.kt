package com.example.app_dich_quet_van_ban.presentation.screens

import android.util.Log
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.example.app_dich_quet_van_ban.presentation.theme.AppDichQuetVanBanTheme
import com.example.app_dich_quet_van_ban.presentation.viewmodel.CaptureViewModel
import java.io.File

@Composable
fun CameraScreen(
    onTextScanned: (String) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: CaptureViewModel = viewModel() // Khai báo ViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val imageCapture = remember { ImageCapture.Builder().build() }


    // Lấy màu Primary của hệ thống để dùng cho Library cắt ảnh
    val primaryColorArgb = MaterialTheme.colorScheme.primary.toArgb()
    val onPrimaryColorArgb = MaterialTheme.colorScheme.onPrimary.toArgb()

    // Launcher xử lý Cắt ảnh (Chỉ lo phần UI Cắt ảnh)
    val cropImageLauncher = rememberLauncherForActivityResult(
        CropImageContract()
    ) { result ->
        if (result.isSuccessful) {
            result.uriContent?.let { uri ->
                // ĐẨY LOGIC XỬ LÝ SANG VIEWMODEL
                viewModel.processImage(context, uri) { scannedText ->
                    onTextScanned(scannedText)
                }
            }
        }
    }

    // Kiểm tra quyền (Giữ nguyên logic của bạn)
    val hasPermission = remember {
        ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    if (!hasPermission) {
        Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) {
            Text("Vui lòng cấp quyền Camera trong cài đặt", color = MaterialTheme.colorScheme.onSurface)
        }
        return
    }

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // Preview Camera (Giữ nguyên giao diện)
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { previewView ->
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = androidx.camera.core.Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageCapture)
                    } catch (exc: Exception) { Log.e("Camera", "Lỗi: ${exc.message}") }
                }, ContextCompat.getMainExecutor(context))
            }
        )

        // 2. Lớp phủ Header (Nút Back)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilledIconButton(
                onClick = onNavigateBack,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = Color.Black.copy(alpha = 0.5f) // Nền tối mờ để nổi bật icon
                )
            ) {
                Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
            }
        }

        // 3. Hiển thị Loading khi đang xử lý OCR
        if (viewModel.isProcessing) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Đang nhận diện...", color = Color.White)
                }
            }
        }

        // Nút Chụp
        Button(
            onClick = {
                val file = File(context.cacheDir, "temp_${System.currentTimeMillis()}.jpg")
                val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

                imageCapture.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            outputFileResults.savedUri?.let { uri ->
                                // Gọi launcher cắt ảnh
                                cropImageLauncher.launch(
                                    CropImageContractOptions(uri, CropImageOptions(
                                        guidelines = CropImageView.Guidelines.ON,
                                        activityTitle = "Cắt lấy phần văn bản",
                                        toolbarColor = primaryColorArgb,
                                        toolbarTitleColor = onPrimaryColorArgb
                                    ))
                                )
                            }
                        }
                        override fun onError(exception: ImageCaptureException) {
                            Log.e("Camera", "Lỗi chụp: ${exception.message}")
                        }
                    }
                )
            },
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 50.dp),
            enabled = !viewModel.isProcessing // Vô hiệu hóa khi đang quét
        ) {
            Text(if (viewModel.isProcessing) "Đang quét..." else "Chụp và Cắt")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CameraScreenPreview() {
    AppDichQuetVanBanTheme {
        CameraScreen(
            onTextScanned = {},
            onNavigateBack = {}
        )
    }
}