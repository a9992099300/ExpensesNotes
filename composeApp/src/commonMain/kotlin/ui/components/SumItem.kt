package ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import ui.themes.AppTheme
import utils.formatPrice

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SumItem(sum: Long, imageVector: ImageVector, tint: Color) {
    val sumCounter by animateIntAsState(
        targetValue = sum.toInt(),
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        )
    )
    Card(
        modifier = Modifier.padding(horizontal = 8.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(
            1.dp,
            AppTheme.colors.activeBorder
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                tint = tint
            )
            CommonText(
                modifier = Modifier,
                text = sumCounter.toLong().formatPrice(),
            )
        }
    }
}