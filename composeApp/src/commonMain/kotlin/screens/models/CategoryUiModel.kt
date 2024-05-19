package screens.models

import androidx.compose.runtime.Immutable

@Immutable
data class CategoryUiModel(
    val typeCategory: TypeCategory,
    val title: String
)

enum class TypeCategory {
    DAY,
    PERIOD,
    MONTH,
    YEAR
}
