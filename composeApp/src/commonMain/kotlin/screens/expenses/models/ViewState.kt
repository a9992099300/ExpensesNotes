package screens.expenses.models

import androidx.compose.runtime.Immutable
import screens.models.CategoryUiModel
import screens.models.ExpensesStateScreen
import screens.models.TypePeriod
import screens.models.TypeTab

@Immutable
data class ExpensesContentState(
    val viewState: ViewState = ViewState.Loading,
    val categories: List<CategoryUiModel> = emptyList(),
    val currentCategory: TypePeriod = TypePeriod.DAY,
    val dateText: String = "",
    val currentTabs: TypeTab = TypeTab.EXPENSES,
    val stateScreen: ExpensesStateScreen = ExpensesStateScreen.EXPENSES_LIST,
    val sum: String = ""
)

sealed class ViewState {
    object ShowContent : ViewState()
    object Loading : ViewState()
    object Error : ViewState()
    object NoItems : ViewState()
}