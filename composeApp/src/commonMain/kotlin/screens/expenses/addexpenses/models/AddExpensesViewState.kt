package screens.expenses.addexpenses.models

import androidx.compose.runtime.Immutable
import screens.expenses.models.DateText
import screens.expenses.models.ExpensesTag
import screens.expenses.models.ViewState
import screens.expenses.models.getExpensesTags
import screens.models.CategoryUiModel
import screens.models.TypePeriod
import screens.models.TypeTab

@Immutable
data class AddExpensesViewState(
    val expensesViewState: ViewState = ViewState.Loading,
    val categories: List<CategoryUiModel> = emptyList(),
    val currentCategory: TypePeriod = TypePeriod.DAY,
    val dateText: DateText = DateText(),
    val currentTabs: TypeTab = TypeTab.EXPENSES,
    val sum: Long = 0,
    val currentTag: ExpensesTag = ExpensesTag.Home(),
    val comment: String = "",
    val tags: List<ExpensesTag> = getExpensesTags(),
)
