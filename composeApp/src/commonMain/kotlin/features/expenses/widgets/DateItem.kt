package features.expenses.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import features.expenses.models.ItemsUiModel


@Composable
fun DateItem(
    item: ItemsUiModel
) {
    Text(
        modifier = Modifier.padding(16.dp, 4.dp).fillMaxWidth(),
        text = "${item.date.day} ${item.date.month}",
        textAlign = TextAlign.Center
    )
}