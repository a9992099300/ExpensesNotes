package screens.expenses.addexpenses.models

import androidx.compose.runtime.Immutable
import screens.expenses.models.ViewState
import screens.models.CategoryUiModel
import screens.models.TypePeriod
import screens.models.TypeTab

@Immutable
data class AddExpensesViewState(
    val expensesViewState: ViewState = ViewState.Loading,
    val categories: List<CategoryUiModel> = emptyList(),
    val currentCategory: TypePeriod = TypePeriod.DAY,
    val dateText: String = "",
    val currentTabs: TypeTab = TypeTab.EXPENSES,
)
