package screens.models

import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource

@Immutable
data class CategoryUiModel @OptIn(ExperimentalResourceApi::class) constructor(
    val typePeriod: TypePeriod,
    val title: StringResource,
)

enum class TypePeriod {
    DAY,
    PERIOD,
    MONTH,
    YEAR
}
