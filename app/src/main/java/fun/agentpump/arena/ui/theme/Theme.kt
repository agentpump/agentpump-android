package `fun`.agentpump.arena.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// AgentPump Colors
val DarkBackground = Color(0xFF0A0A0F)
val DarkSurface = Color(0xFF111118)
val DarkCard = Color(0xFF1A1A24)
val NeonGreen = Color(0xFF00FF88)
val NeonBlue = Color(0xFF00CCFF)
val NeonPurple = Color(0xFF6366F1)
val NeonOrange = Color(0xFFF59E0B)
val NeonPink = Color(0xFFEC4899)
val NeonTeal = Color(0xFF10B981)
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFF888888)
val TextMuted = Color(0xFF666666)
val LiveRed = Color(0xFFFF3B30)

private val DarkColorScheme = darkColorScheme(
    primary = NeonGreen,
    secondary = NeonBlue,
    tertiary = NeonPurple,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkCard,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary
)

@Composable
fun AgentPumpTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography(),
        content = content
    )
}
