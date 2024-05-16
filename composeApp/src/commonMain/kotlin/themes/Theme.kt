package themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun MainTheme(
    style: AppStyle = AppStyle.Purple,
    textSize: AppSize = AppSize.Medium,
    paddingSize: AppSize = AppSize.Medium,
    corners: AppCorners = AppCorners.Rounded,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = when (darkTheme) {
        true -> {
            when (style) {
                AppStyle.Purple -> purpleDarkPalette
                AppStyle.Blue -> blueDarkPalette
                AppStyle.Orange -> orangeDarkPalette
                AppStyle.Red -> redDarkPalette
                AppStyle.Green -> greenDarkPalette
            }
        }
        false -> {
            when (style) {
                AppStyle.Purple -> purpleLightPalette
                AppStyle.Blue -> blueLightPalette
                AppStyle.Orange -> orangeLightPalette
                AppStyle.Red -> redLightPalette
                AppStyle.Green -> greenLightPalette
            }
        }
    }

    val typography = JetHabitTypography(
        heading = TextStyle(
            fontSize = when (textSize) {
                AppSize.Small -> 24.sp
                AppSize.Medium -> 28.sp
                AppSize.Big -> 32.sp
            },
            fontWeight = FontWeight.Bold
        ),
        body = TextStyle(
            fontSize = when (textSize) {
                AppSize.Small -> 14.sp
                AppSize.Medium -> 16.sp
                AppSize.Big -> 18.sp
            },
            fontWeight = FontWeight.Normal
        ),
        toolbar = TextStyle(
            fontSize = when (textSize) {
                AppSize.Small -> 14.sp
                AppSize.Medium -> 16.sp
                AppSize.Big -> 18.sp
            },
            fontWeight = FontWeight.Medium
        ),
        caption = TextStyle(
            fontSize = when (textSize) {
                AppSize.Small -> 10.sp
                AppSize.Medium -> 12.sp
                AppSize.Big -> 14.sp
            }
        )
    )

    val shapes = AppShape(
        padding = when (paddingSize) {
            AppSize.Small -> 12.dp
            AppSize.Medium -> 16.dp
            AppSize.Big -> 20.dp
        },
        cornersStyle = when (corners) {
            AppCorners.Flat -> RoundedCornerShape(0.dp)
            AppCorners.Rounded -> RoundedCornerShape(8.dp)
        }
    )

    val images = AppImage(
        mainIcon =  null, //if (darkTheme) R.drawable.ic_baseline_mood_24 else R.drawable.ic_baseline_mood_bad_24,
        mainIconDescription = if (darkTheme) "Good Mood" else "Bad Mood"
    )

    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalJetHabitTypography provides typography,
        LocalAppShape provides shapes,
        LocalAppImage provides images,
        content = content
    )
}