package com.v2ray.ang.ui.compose

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.v2ray.ang.AppConfig
import com.v2ray.ang.handler.MmkvManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private val LightColor = lightColorScheme(
    primary = Color(0xFF000000), // Black
    onPrimary = Color(0xFFFFFFFF), // White
    primaryContainer = Color(0xFFE0E0E0), // Light Gray
    onPrimaryContainer = Color(0xFF000000), // Black
    secondary = Color(0xFFf97910), // Orange
    onSecondary = Color(0xFFFFFFFF), // White
    secondaryContainer = Color(0xFFFFE8D6), // Pale Orange
    onSecondaryContainer = Color(0xFF2B1700), // Dark Brown
    tertiary = Color(0xFF009966), // Green
    onTertiary = Color(0xFFFFFFFF), // White
    tertiaryContainer = Color(0xFFA0F2D0), // Light Green
    onTertiaryContainer = Color(0xFF00201A), // Dark Teal
    error = Color(0xFFBA1A1A), // Red
    errorContainer = Color(0xFFFFDAD6), // Light Red
    onError = Color(0xFFFFFFFF), // White
    onErrorContainer = Color(0xFF410002), // Dark Red
    background = Color(0xFFFFFFFF), // White
    onBackground = Color(0xFF1C1B1F), // Near Black
    surface = Color(0xFFFFFFFF), // White
    onSurface = Color(0xFF1C1B1F), // Near Black
    surfaceVariant = Color(0xFFE7E0EC), // Light Purple Gray
    onSurfaceVariant = Color(0xFF49454F), // Dark Gray
    outline = Color(0xFF79747E), // Medium Gray
    outlineVariant = Color(0xFFCAC4D0), // Light Gray
    inverseSurface = Color(0xFF313033), // Dark Gray
    inverseOnSurface = Color(0xFFF4EFF4), // Very Light Gray
    inversePrimary = Color(0xFFC0C0C0), // Silver Gray
    scrim = Color(0xFF000000), // Black
    surfaceTint = Color(0xFF000000), // Black
    surfaceContainerLowest = Color(0xFFFFFFFF), // White
    surfaceContainerLow = Color(0xFFF7F7F7), // Very Light Gray
    surfaceContainer = Color(0xFFF1F1F1), // Light Gray
    surfaceContainerHigh = Color(0xFFEBEBEB), // Light Gray
    surfaceContainerHighest = Color(0xFFE5E5E5), // Light Gray
)

/* Палитра Uziwi «Aurora Glass» — та же, что в мини-приложении и в боте.
 * Менять только здесь: Material3 разводит эти цвета по всему интерфейсу сам.
 *
 * ground  #08081A  — тёмный фон, поверх него живёт градиент
 * indigo  #6366F1  — основной акцент
 * violet  #7C3AED  — второй акцент, из него градиенты
 * peri    #A5B4FC  — светлый барвинок, им подписи и обводки
 * text    #E0E0F0 / muted #6B6B8F / green #4ADE80 / red #FCA5A5
 */
private val DarkColor = darkColorScheme(
    primary = Color(0xFF6366F1),              // indigo — главная кнопка
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF7C3AED),     // violet
    onPrimaryContainer = Color(0xFFE0E0F0),
    secondary = Color(0xFFA5B4FC),            // peri
    onSecondary = Color(0xFF1B1836),
    secondaryContainer = Color(0xFF2A2552),
    onSecondaryContainer = Color(0xFFE0E0F0),
    tertiary = Color(0xFF4ADE80),             // зелёный «подключено»
    onTertiary = Color(0xFF06281A),
    tertiaryContainer = Color(0xFF14532D),
    onTertiaryContainer = Color(0xFFBBF7D0),
    error = Color(0xFFFCA5A5),
    onError = Color(0xFF3F0A0A),
    errorContainer = Color(0xFF5A1A1A),
    onErrorContainer = Color(0xFFFFDAD6),
    // Фон прозрачный: под интерфейсом лежит фирменный градиент (AuroraBackground).
    // Сделай его непрозрачным — градиент пропадёт, и приложение станет обычным.
    background = Color(0x0008081A),
    onBackground = Color(0xFFE0E0F0),
    surface = Color(0xB3141230),               // стекло: 70% непрозрачности
    onSurface = Color(0xFFE0E0F0),
    surfaceVariant = Color(0xB31B1836),
    onSurfaceVariant = Color(0xFF9E9EC4),
    outline = Color(0x59A5B4FC),               // обводки — барвинок на 35%
    outlineVariant = Color(0x33A5B4FC),
    inverseSurface = Color(0xFFE0E0F0),
    inverseOnSurface = Color(0xFF08081A),
    inversePrimary = Color(0xFF4F46E5),
    scrim = Color(0xFF000000),
    surfaceTint = Color(0xFF6366F1),
    // Слои «стекла»: чем выше элемент, тем светлее подложка.
    // Все полупрозрачные (0xB3 ≈ 70%), чтобы сквозь них читался градиент.
    surfaceContainerLowest = Color(0xB30B0A1C),
    surfaceContainerLow = Color(0xB3121030),
    surfaceContainer = Color(0xB3161436),
    surfaceContainerHigh = Color(0xB31D1A44),
    surfaceContainerHighest = Color(0xB3252152),
)

// Semantic Colors
val colorPing = Color(0xFF009966) // Green
val colorPingRed = Color(0xFFFF0099) // Pink Red
val colorConfigType = Color(0xFFf97910) // Orange
val colorFabActive = Color(0xFFf97910) // Orange
val colorFabInactiveLight = Color(0xFF9C9C9C) // Gray
val colorFabInactiveDark = Color(0xFF646464) // Dark Gray
val dividerColorLight = Color(0xFFE0E0E0) // Light Gray
val dividerColorDark = Color(0xFF424242) // Dark Gray

// Toast Colors 70%
val toastNormalBgLight = Color(0xB3353A3E) // Dark Gray
val toastNormalBgDark = Color(0xB34A4F54) // Darker Gray
val toastSuccessBg = Color(0xB3388E3C) // Green
val toastErrorBg = Color(0xB3D50000) // Red
val toastInfoBg = Color(0xB33F51B5) // Indigo Blue
val toastIconCircleBg = Color(0x33FFFFFF) // Semi-transparent White
val toastTextColor = Color.White // White

object ThemeManager {
    private val _themeMode = MutableStateFlow(
        MmkvManager.decodeSettingsString(AppConfig.PREF_UI_MODE_NIGHT, "0") ?: "0"
    )
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    private val _dynamicColorEnabled = MutableStateFlow(
        MmkvManager.decodeSettingsBool(AppConfig.PREF_DYNAMIC_COLOR, true)
    )
    val dynamicColorEnabled: StateFlow<Boolean> = _dynamicColorEnabled.asStateFlow()

    fun setThemeMode(mode: String) {
        MmkvManager.encodeSettings(AppConfig.PREF_UI_MODE_NIGHT, mode)
        _themeMode.value = mode
    }

    fun setDynamicColorEnabled(enabled: Boolean) {
        MmkvManager.encodeSettings(AppConfig.PREF_DYNAMIC_COLOR, enabled)
        _dynamicColorEnabled.value = enabled
    }

    fun refresh() {
        _themeMode.value =
            MmkvManager.decodeSettingsString(AppConfig.PREF_UI_MODE_NIGHT, "0") ?: "0"
        _dynamicColorEnabled.value =
            MmkvManager.decodeSettingsBool(AppConfig.PREF_DYNAMIC_COLOR, true)
    }
}

@Composable
fun resolveDarkTheme(): Boolean {
    // У Uziwi светлой темы нет и не будет: «Aurora Glass» — тёмный стиль,
    // на белом фоне он рассыпается. Системную настройку намеренно
    // игнорируем, иначе половина людей увидит чужое приложение.
    return true
}

val LocalDarkTheme = compositionLocalOf { false }

/** Фон Uziwi: тёмная основа и два пятна северного сияния по углам.
 *
 * Рисуется один раз под всем содержимым. Не картинка и не анимация —
 * два радиальных градиента: это ничего не весит, не греет телефон
 * и выглядит одинаково на любом экране. */
@Composable
private fun AuroraBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF08081A))
            .drawBehind {
                // Сверху слева — фиолетовое, справа сверху — индиго.
                // Радиус больше экрана: край пятна не должен попадать в кадр.
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x2E7C3AED), Color(0x007C3AED)),
                        center = Offset(size.width * 0.12f, 0f),
                        radius = size.height * 0.62f,
                    ),
                    radius = size.height * 0.62f,
                    center = Offset(size.width * 0.12f, 0f),
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x246366F1), Color(0x006366F1)),
                        center = Offset(size.width * 0.96f, size.height * 0.10f),
                        radius = size.height * 0.55f,
                    ),
                    radius = size.height * 0.55f,
                    center = Offset(size.width * 0.96f, size.height * 0.10f),
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x1FA5B4FC), Color(0x00A5B4FC)),
                        center = Offset(size.width * 0.08f, size.height),
                        radius = size.height * 0.45f,
                    ),
                    radius = size.height * 0.45f,
                    center = Offset(size.width * 0.08f, size.height),
                )
            }
    ) { content() }
}

@Composable
fun AppTheme(
    darkTheme: Boolean = resolveDarkTheme(),
    content: @Composable () -> Unit
) {
    // Динамические цвета Android намеренно не используем: они подменяют
    // палитру обоями телефона, и от фирменного стиля ничего не остаётся.
    val colorScheme = DarkColor
    val snackbarController = rememberAppSnackbarController()

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val activity = view.context as? Activity ?: return@SideEffect
            val window = activity.window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(
        LocalDarkTheme provides darkTheme,
        LocalAppSnackbar provides snackbarController
    ) {
        MaterialTheme(
            colorScheme = colorScheme
        ) {
            AuroraBackground {
                Box(modifier = Modifier.fillMaxSize()) {
                    AppSnackbarBridge(controller = snackbarController)
                    content()
                    AppSnackbarHost(hostState = snackbarController.hostState)
                }
            }
        }
    }
}
