package screens.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import screens.models.CategoryUiModel
import screens.models.TypeCategory

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CommonFilterChip(
    categoryUiModel: CategoryUiModel,
    currentCategory: TypeCategory,
    onClick: (TypeCategory) -> Unit
) {
    FilterChip(
        modifier = Modifier
            .padding(4.dp),
        selected = categoryUiModel.typeCategory == currentCategory,
        onClick = { onClick.invoke(categoryUiModel.typeCategory) },
        leadingIcon = if (categoryUiModel.typeCategory == currentCategory) {
            {
                Icon(Icons.Outlined.Check, contentDescription = null)
            }
        } else {
            null
        }
    ) {
        CommonText(categoryUiModel.title)
    }
}