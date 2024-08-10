package features.expenses.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import features.expenses.models.ItemsUiModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import themes.AppTheme


@OptIn(ExperimentalResourceApi::class)
@Composable
fun ExpensesItem(
    item: ItemsUiModel
) {
    Card(
        modifier = Modifier.padding(16.dp, 4.dp).fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.surface,
        border = BorderStroke(2.dp, AppTheme.colors.activeBackground)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp, 8.dp, 16.dp, 8.dp)
                .height(32.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(item.tag.icon),
                contentDescription = stringResource(item.tag.name)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier.padding(32.dp, 0.dp),
                text = item.date.time,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(0.4f))

            Text(
                modifier = Modifier.width(100.dp),
                textAlign = TextAlign.End,
                text = if (item.isExpenses) {
                    "- " + item.sum.toString() + " Р"
                } else {
                    item.sum.toString() + " Р"
                }
            )
        }
    }
}