package screens.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import themes.AppTheme
import androidx.compose.material.MaterialTheme

@Composable
fun CommonText(
    text: String,
    modifier: Modifier = Modifier,
    size: Int = 16,
    textAlign: TextAlign = TextAlign.Center,
    textStyle: TextStyle = MaterialTheme.typography.h4
) {
    Text(
        text = text,
        modifier = modifier,
        color = AppTheme.colors.primaryText,
        textAlign = textAlign,
        style = textStyle,
        fontSize = size.sp,
    )
}