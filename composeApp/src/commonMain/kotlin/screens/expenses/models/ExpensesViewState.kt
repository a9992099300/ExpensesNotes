package screens.expenses.models

import androidx.compose.runtime.Immutable
import screens.models.CategoryUiModel
import screens.models.TypeCategory
import screens.models.TypeTab

@Immutable
data class ContentState(
    val expensesViewState: ExpensesViewState = Loading,
    val title: String = "",
    val categories: List<CategoryUiModel> = emptyList(),
    val currentCategory: TypeCategory = TypeCategory.DAY,
    val dateText: String = "",
    val currentTabs: TypeTab = TypeTab.EXPENSES,
) : ExpensesViewState()

sealed class ExpensesViewState {
    object ShowContent : ExpensesViewState()
    object Loading : ExpensesViewState()
    object Error : ExpensesViewState()
    object NoItems : ExpensesViewState()
}