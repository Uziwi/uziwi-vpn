package com.v2ray.ang.ui.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.v2ray.ang.R
import com.v2ray.ang.handler.MmkvManager

/**
 * Главный экран Uziwi: одна кнопка и состояние.
 *
 * Заменяет собой список серверов из v2rayNG. Смысл замены: наш клиент
 * получает готовую подписку из бота, ему не нужно ни добавлять серверы
 * руками, ни листать их вкладками. Всё, что ему нужно знать, — включено
 * или нет и через какую страну.
 *
 * Список никуда не делся: он остался в коде и доступен через меню.
 * Убрана только его роль главного экрана — чтобы не пугать человека,
 * который поставил VPN, а увидел таблицу с пингами.
 */
@Composable
fun UziwiConnectScreen(
    isRunning: Boolean,
    statusText: String,
    selectedGuid: String?,
    hasServers: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Имя выбранного сервера читаем прямо из хранилища. MMKV лежит
    // в памяти, отображённой на файл, — это не поход на диск, и делать
    // ради одной строки отдельное поле в состоянии незачем.
    val serverName = remember(selectedGuid) {
        selectedGuid?.let { MmkvManager.decodeServerConfig(it)?.remarks }.orEmpty()
    }

    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Uziwi VPN",
            color = Color(0xFFA5B4FC),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 3.sp,
        )

        Box(modifier = Modifier.size(0.dp, 26.dp))

        // Сама кнопка. Включено — зелёный ободок и тёмная сердцевина,
        // выключено — фирменный градиент. Разница видна с расстояния
        // вытянутой руки, и это главное: человек смотрит на неё секунду.
        Box(
            modifier = Modifier
                .size(176.dp)
                .clip(CircleShape)
                .background(
                    if (isRunning) {
                        Brush.linearGradient(
                            listOf(Color(0xFF14532D), Color(0xFF166534))
                        )
                    } else {
                        Brush.linearGradient(
                            listOf(Color(0xFF6366F1), Color(0xFF7C3AED))
                        )
                    }
                )
                .border(
                    BorderStroke(
                        if (isRunning) 3.dp else 1.dp,
                        if (isRunning) Color(0xFF4ADE80) else Color(0x40E0E0F0),
                    ),
                    CircleShape,
                )
                .clickable(enabled = hasServers, onClick = onToggle),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(
                    if (isRunning) R.drawable.ic_stop_24dp else R.drawable.ic_play_24dp
                ),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(64.dp),
            )
        }

        Box(modifier = Modifier.size(0.dp, 24.dp))

        Text(
            text = if (isRunning) "Подключено" else "Не подключено",
            color = if (isRunning) Color(0xFF4ADE80) else Color(0xFFE0E0F0),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
        )

        Box(modifier = Modifier.size(0.dp, 6.dp))

        Text(
            text = statusText,
            color = Color(0xFF9E9EC4),
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
        )

        if (serverName.isNotEmpty()) {
            Box(modifier = Modifier.size(0.dp, 20.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color(0x66141230))
                    .border(
                        BorderStroke(1.dp, Color(0x59A5B4FC)),
                        RoundedCornerShape(999.dp),
                    )
                    .padding(horizontal = 16.dp, vertical = 9.dp)
            ) {
                Text(text = serverName, color = Color(0xFFE0E0F0), fontSize = 14.sp)
            }
        }

        if (!hasServers) {
            Box(modifier = Modifier.size(0.dp, 22.dp))
            Text(
                text = "Подписка пока не добавлена.\n" +
                    "Откройте приложение Uziwi в Telegram и нажмите «Подключить».",
                color = Color(0xFF9E9EC4),
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
