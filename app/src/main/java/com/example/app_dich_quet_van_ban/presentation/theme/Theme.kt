package com.example.app_dich_quet_van_ban.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,
    tertiary = TertiaryLight,
    onTertiary = OnTertiaryLight,
    tertiaryContainer = TertiaryContainerLight,
    onTertiaryContainer = OnTertiaryContainerLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    error = ErrorLight,
    onError = OnErrorLight
)

/**
 * TỰ ĐỘNG TÍNH TOÁN MÀU:
 * Giúp app đổi màu đồng bộ khi người dùng tùy chỉnh màu Primary
 */
private fun generateDynamicColorScheme(primary: Color) = lightColorScheme(
    primary = primary,
    onPrimary = Color.White,
    primaryContainer = primary.copy(alpha = 0.12f),
    onPrimaryContainer = primary,
    // Trộn màu Primary với Tím để tạo màu Secondary hài hòa
    secondary = Color(ColorUtils.blendARGB(primary.toArgb(), Color(0xFF9C27B0).toArgb(), 0.5f)),
    onSecondary = Color.White,
    background = BackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    onSurfaceVariant = OnSurfaceVariantLight
)

@Composable
fun AppDichQuetVanBanTheme(
    primaryColor: Color = PrimaryLight, // Mặc định là Google Blue
    content: @Composable () -> Unit
) {
    val colorScheme = if (primaryColor == PrimaryLight) {
        LightColorScheme
    } else {
        generateDynamicColorScheme(primaryColor)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}