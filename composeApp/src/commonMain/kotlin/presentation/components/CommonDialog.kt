package presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import expensenotes.composeapp.generated.resources.Res
import expensenotes.composeapp.generated.resources.cancel
import expensenotes.composeapp.generated.resources.ok
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import presentation.themes.AppTheme

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CommonDialog(
    title: StringResource?,
    text: StringResource?,
    onSuccess: () -> Unit,
    onCanceled: () -> Unit,
) {

    AlertDialog(
        onDismissRequest = onCanceled,
        modifier = Modifier.width(400.dp).height(200.dp),
        shape = RoundedCornerShape(20.dp),
        title = {
            CommonText(stringResource = text, modifier = Modifier.fillMaxWidth(), size = 18)
        },
        text = {
            CommonText(stringResource = title, modifier = Modifier.fillMaxWidth(), size = 18)
        },
        buttons = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(onClick = onCanceled) {
                    CommonText(
                        stringResource = Res.string.ok,
                        modifier = Modifier
                            .background(
                                color = AppTheme.colors.primaryAction,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 16.dp)
                    )
                }

                TextButton(onClick = onSuccess) {
                    CommonText(stringResource = Res.string.cancel)
                }
            }
        }
    )
}