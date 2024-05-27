package themes

import LocalColorProvider
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import palette

@Composable
internal fun MainTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    if (!useDarkTheme) {
        CompositionLocalProvider(
            LocalColorProvider provides palette,
            LocalAppTypography provides typography,
            content = content
        )
    } else {
        CompositionLocalProvider(
            LocalColorProvider provides palette,
            LocalAppTypography provides typography,
            content = content
        )
    }
}

val typography = AppTypography(
    heading = TextStyle(fontSize = 28.sp),
    body = TextStyle(fontSize = 16.sp),
    toolbar = TextStyle(fontSize = 16.sp),
    caption = TextStyle(fontSize = 12.sp),
    )