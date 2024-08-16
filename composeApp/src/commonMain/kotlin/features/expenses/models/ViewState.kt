package features.expenses.models

import androidx.compose.runtime.Immutable
import presentation.models.CategoryUiModel
import presentation.models.TypePeriod
import presentation.models.TypeTab
import presentation.utils.Dates
import kotlinx.datetime.LocalDateTime
import utils.addZeroForTime

@Immutable
data class ExpensesContentState(
    val viewState: ViewState = ViewState.Loading,
    val categories: List<CategoryUiModel> = emptyList(),
    val items: List<ItemsUiModel> = emptyList(),
    val currentCategory: TypePeriod = TypePeriod.DAY,
    val dateText: DateText = DateText(),
    val currentTabs: TypeTab = TypeTab.EXPENSES,
    val expensesSum: Long = 0,
    val incomesSum: Long = 0,
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
    val monthOnly: String = "",
    val year: String = "",
    val time: String = ""
)

fun getDateText(date: LocalDateTime) =
    DateText(
        day = date.dayOfMonth.toString(),
        month = Dates.getMonthName(date.monthNumber),
        monthOnly =  Dates.getMonthName2(date.monthNumber),
        year = date.year.toString(),
        time ="${date.time.hour.addZeroForTime()}:${date.time.minute.addZeroForTime()}"
    )