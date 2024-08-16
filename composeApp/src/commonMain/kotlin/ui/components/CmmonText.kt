package ui.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import ui.themes.AppTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.text.AnnotatedString
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CommonText(
    text: String?= null,
    stringResource: StringResource? = null,
    modifier: Modifier = Modifier,
    size: Int = 16,
    textAlign: TextAlign = TextAlign.Center,
    textStyle: TextStyle = MaterialTheme.typography.h4
) {
    Text(
        text = text ?: stringResource?.let { stringResource(it) } ?: "",
        modifier = modifier,
        color = AppTheme.colors.primaryTextColor,
        textAlign = textAlign,
        style = textStyle,
        fontSize = size.sp,
    )
}

@Composable
fun CommonText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    size: Int = 16,
    textAlign: TextAlign = TextAlign.Center,
    textStyle: TextStyle = MaterialTheme.typography.h4
) {
    Text(
        text = text,
        modifier = modifier,
        color = AppTheme.colors.primaryTextColor,
        textAlign = textAlign,
        style = textStyle,
        fontSize = size.sp,
    )
}
