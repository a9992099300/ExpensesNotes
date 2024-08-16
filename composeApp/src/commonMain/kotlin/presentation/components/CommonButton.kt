package presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import presentation.themes.AppTheme


@Composable
fun CommonButton(
    text: String,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
    imageVector: ImageVector? = null,
    backgroundColor: Color = AppTheme.colors.primaryAction,
    elevation: ButtonElevation = ButtonDefaults.elevation(defaultElevation = 4.dp),
    onClickButton: () -> Unit,
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        onClick = onClickButton,
        colors = ButtonDefaults.buttonColors(
            contentColor = AppTheme.colors.secondaryTextColor,
            backgroundColor = backgroundColor,
        ),
        elevation = elevation,
        shape = RoundedCornerShape(24.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = AppTheme.colors.secondaryTextColor
            )
        } else {
            Row(
                modifier = Modifier.align(Alignment.CenterVertically),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (imageVector != null) {
                    Image(
                        imageVector,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(8.dp, 0.dp)
                            .fillMaxHeight()
                    )
                }
                Text(
                    text,
                    modifier = Modifier.fillMaxHeight(),
                    style = MaterialTheme.typography.h4,
                    fontSize = 18.sp,
                    color = AppTheme.colors.primaryTextColor
                )

            }

        }
    }
}