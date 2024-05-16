package themes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

data class AppColors(
    val primaryText: Color,
    val primaryBackground: Color,
    val secondaryText: Color,
    val secondaryBackground: Color,
    val tintColor: Color,
    val controlColor: Color,
    val errorColor: Color,
)

data class JetHabitTypography(
    val heading: TextStyle,
    val body: TextStyle,
    val toolbar: TextStyle,
    val caption: TextStyle
)

data class AppShape(
    val padding: Dp,
    val cornersStyle: Shape
)

data class AppImage(
    val mainIcon: Int?,
    val mainIconDescription: String
)

object AppTheme {
    internal val colors: AppColors
        @Composable
        internal get() = LocalAppColors.current

    internal val typography: JetHabitTypography
        @Composable
        internal get() = LocalJetHabitTypography.current

    internal val shapes: AppShape
        @Composable
        internal get() = LocalAppShape.current

    internal val images: AppImage
        @Composable
        internal get() = LocalAppImage.current
}

enum class AppStyle {
    Purple, Orange, Blue, Red, Green
}

enum class AppSize {
    Small, Medium, Big
}

enum class AppCorners {
    Flat, Rounded
}

internal val LocalAppColors = staticCompositionLocalOf<AppColors> {
    error("No colors provided")
}

internal val LocalJetHabitTypography = staticCompositionLocalOf<JetHabitTypography> {
    error("No font provided")
}

internal val LocalAppShape = staticCompositionLocalOf<AppShape> {
    error("No shapes provided")
}

internal val LocalAppImage = staticCompositionLocalOf<AppImage> {
    error("No images provided")
}