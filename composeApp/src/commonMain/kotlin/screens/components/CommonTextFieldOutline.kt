package screens.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import themes.AppTheme

@Composable
fun CommonTextFieldOutline(
    text: String,
    hint: String,
    enabled: Boolean = true,
    isError: Boolean = false,
    height: Int = 56,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChanged: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp),
        value = text,
        onValueChange = {
            onValueChanged.invoke(it)
        },
        label = { Text(hint) },
        enabled = enabled,
        shape = RoundedCornerShape(10.dp),
        isError = isError,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = AppTheme.colors.primaryTextColor,
            focusedLabelColor = AppTheme.colors.primaryTextColor,
        )
    )
}