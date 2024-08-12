package features.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import features.models.CategoryUiModel
import features.models.TypePeriod
import themes.AppTheme

@OptIn(ExperimentalMaterialApi::class, ExperimentalResourceApi::class)
@Composable
fun CommonFilterChip(
    categoryUiModel: CategoryUiModel,
    currentCategory: TypePeriod,
    onClick: (TypePeriod) -> Unit
) {
    FilterChip(
        modifier = Modifier
            .padding(4.dp),
        colors = ChipDefaults.filterChipColors(
            selectedBackgroundColor = AppTheme.colors.activeBackground,
            backgroundColor = AppTheme.colors.activeBackground.copy(alpha = 0.5F)
        ),
        selected = categoryUiModel.typePeriod == currentCategory,
        onClick = { onClick.invoke(categoryUiModel.typePeriod) },
        leadingIcon = if (categoryUiModel.typePeriod == currentCategory) {
            {
                Icon(Icons.Outlined.Check, contentDescription = null)
            }
        } else {
            null
        }
    ) {
        CommonText(stringResource = categoryUiModel.title)
    }
}