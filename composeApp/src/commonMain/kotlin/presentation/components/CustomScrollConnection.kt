package presentation.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.sign

class CustomScrollConnection(
    downThreshold: Dp = 0.dp,
    upThreshold: Dp = 0.dp,
    density: Density
) : NestedScrollConnection {

    var isScrollingDown by mutableStateOf(false)
        private set

    private val downThresholdPx = with(density) { -downThreshold.toPx() }
    private val upThresholdPx = with(density) { upThreshold.toPx() }
    private var isScrollingDownDelta = 0f

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.y
        updateState(delta)
        return Offset.Zero
    }

    private fun updateState(delta: Float) {
        val isChangingDirections = isScrollingDownDelta.sign!= delta.sign

        isScrollingDownDelta = if (isChangingDirections) {
            delta
        } else {
            isScrollingDownDelta + delta
        }.coerceIn(downThresholdPx, upThresholdPx)

        val isUpScroll = isScrollingDownDelta == upThresholdPx && delta > 0
        val isDownScroll = isScrollingDownDelta == downThresholdPx && delta < 0

        val continueDownScroll = isScrollingDown && !isUpScroll
        isScrollingDown = continueDownScroll || isDownScroll
    }
}