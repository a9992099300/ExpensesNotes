package features.expenses.models

import androidx.compose.runtime.Immutable
import features.models.CategoryUiModel
import features.models.TypePeriod
import features.models.TypeTab

@Immutable
data class ExpensesContentState(
    val viewState: ViewState = ViewState.Loading,
    val categories: List<CategoryUiModel> = emptyList(),
    val expensesItems: List<ExpensesDataModel> = emptyList(),
    val currentCategory: TypePeriod = TypePeriod.DAY,
    val dateText: DateText = DateText(),
    val currentTabs: TypeTab = TypeTab.EXPENSES,
)

sealed class ViewState {
    object ShowContent : ViewState()
    object Loading : ViewState()
    object Error : ViewState()
    object NoItems : ViewState()
}

@Immutable
data class DateText(
    val day: String = "",
    val month: String = "",
    val year: String = "",
)