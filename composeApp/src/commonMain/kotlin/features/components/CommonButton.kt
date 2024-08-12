package features.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import themes.AppTheme


@Composable
fun CommonButton(
    text: String,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
    onClickButton: () -> Unit,
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(shape = RoundedCornerShape(24.dp)),
        onClick = onClickButton,
        colors = ButtonDefaults.buttonColors(
            contentColor = AppTheme.colors.secondaryTextColor,
            backgroundColor = AppTheme.colors.primaryAction,
        ),
        elevation = ButtonDefaults.elevation(defaultElevation = 4.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = AppTheme.colors.secondaryTextColor
            )
        } else {
            Text(
                text,
                style = MaterialTheme.typography.h4,
                fontSize = 18.sp,
                color = AppTheme.colors.primaryTextColor
            )
        }
    }
}