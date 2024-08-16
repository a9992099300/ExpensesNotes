package features.addexpenses.models

import androidx.compose.runtime.Immutable
import features.expenses.models.DateText
import features.tags.models.ExpensesTag
import features.expenses.models.ViewState
import features.tags.models.getExpensesTags
import presentation.models.CategoryUiModel
import presentation.models.TypePeriod
import presentation.models.TypeTab

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
