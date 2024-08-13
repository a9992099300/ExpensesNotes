package ui.themes

import ColorsApp
import LocalColorProvider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

object AppTheme {
   internal val colors: ColorsApp
        @Composable
        get() = LocalColorProvider.current

    internal val typography: AppTypography
        @Composable
        internal get() = LocalAppTypography.current
}

internal val LocalAppTypography = staticCompositionLocalOf<AppTypography> {
    error("No font provided")
}

enum class AppHabitSize {
    Small, Medium, Big
}

data class AppTypography(
    val heading: TextStyle,
    val body: TextStyle,
    val toolbar: TextStyle,
    val caption: TextStyle
)
