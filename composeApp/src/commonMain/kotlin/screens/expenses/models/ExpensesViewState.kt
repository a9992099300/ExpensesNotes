package screens.expenses.models

import androidx.compose.runtime.Immutable
import screens.models.CategoryUiModel
import screens.models.TypeCategory

sealed class ExpensesViewState {

    object Loading : ExpensesViewState()
    object Error : ExpensesViewState()

    @Immutable
    data class Content(
        val title: String = "",
        val categories: List<CategoryUiModel> = emptyList(),
        val currentCategory: TypeCategory = TypeCategory.DAY,
        val dateText: String = "",
    ) : ExpensesViewState()
    object NoItems: ExpensesViewState()
}