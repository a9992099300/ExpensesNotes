import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class ColorsApp(
    val primaryBackground: Color,
    val secondaryBackground: Color,
    val navbarBackground: Color,
    val activeBackground: Color,
    val primaryAction: Color,
    val primaryTextColor: Color,
    val hintTextColor: Color,
    val highlightTextColor: Color,
    val secondaryTextColor: Color,
    val thirdTextColor: Color,
    val tagColor: Color,
    val tagTextColor: Color,
    val activeBorder: Color,
    val tagColorRed: Color,
    val tagColorGreen: Color
)

val palette = ColorsApp(
    primaryBackground = Color(0xFFFFFFFF),
    secondaryBackground = Color(0xFFF3F4F5),
    navbarBackground = Color(0xFFebf2ff),
    activeBackground = Color(0xFFb8d1ff),
    primaryAction = Color(0xFF85b4ff), //Color(0xFFFd77d31),
    primaryTextColor = Color(0xFF050B18),
    hintTextColor = Color(0xFF696C75),
    highlightTextColor = Color(0xFFF4D144),
    secondaryTextColor = Color(0xFFd5def6),
    thirdTextColor = Color(0xFFEEF2FB),
    tagColor = Color(0x1844A9F4),
    tagTextColor = Color(0xFF44A9F4),
    activeBorder = Color(     0xFF1f6dff),
    tagColorRed = Color(0xFFff1919),
    tagColorGreen = Color(0xFF00d17a)
)

val LocalColorProvider = staticCompositionLocalOf<ColorsApp> {
    //error("No default implementation")
    palette
}