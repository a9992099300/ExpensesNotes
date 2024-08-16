package presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import features.expenses.models.ItemsUiModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import presentation.themes.AppTheme
import utils.formatPrice


@OptIn(ExperimentalResourceApi::class)
@Composable
fun ExpensesItem(
    item: ItemsUiModel
) {
    Card(
        modifier = Modifier.padding(16.dp, 4.dp).fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.surface,
        border = BorderStroke(
            1.dp,
            AppTheme.colors.activeBorder
            // if (item.isExpenses) AppTheme.colors.tagColorRed else AppTheme.colors.tagColorBlue
        ),
        elevation = 5.dp
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
                contentDescription = null
            )

            Text(
                modifier = Modifier.padding(start = 16.dp),
                text =    if (item.tag.name != null) stringResource(item.tag.name!!) else item.tag.nameString,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier.padding(4.dp, 0.dp),
                text = if (item.comment.isNotBlank()) "(${item.comment})" else "",
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = if (item.isExpenses) Icons.Outlined.KeyboardArrowDown else Icons.Outlined.KeyboardArrowUp,
                contentDescription =  if (item.tag.name != null) stringResource(item.tag.name!!) else item.tag.nameString,
                tint = if (item.isExpenses) AppTheme.colors.tagColorRed else AppTheme.colors.tagColorGreen
            )

            Text(
                modifier = Modifier,
                textAlign = TextAlign.End,
                text = item.amount.formatPrice()
            )
        }
    }
}