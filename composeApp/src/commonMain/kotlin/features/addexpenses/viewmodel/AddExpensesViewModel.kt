package features.addexpenses.viewmodel

import androidx.lifecycle.viewModelScope
import base.BaseViewModel
import com.benasher44.uuid.uuid4
import di.Inject.instance
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import features.addexpenses.models.AddExpensesAction
import features.addexpenses.models.AddExpensesEvent
import features.addexpenses.models.AddExpensesViewState
import features.expenses.models.DateText
import features.expenses.models.ItemDataModel
import features.expenses.models.ViewState
import features.expenses.models.getExpensesTags
import features.expenses.models.getIncomesTags
import features.expenses.repository.ExpensesRepository
import ui.models.ActionDate
import ui.models.TypePeriod
import ui.models.TypeTab
import ui.models.getTab
import ui.utils.Dates
import kotlinx.coroutines.launch

class AddExpensesViewModel(
    private val expensesRepository: ExpensesRepository = instance(),
    private val tab: String
) : BaseViewModel<AddExpensesViewState, AddExpensesAction, AddExpensesEvent>(
    initialState = AddExpensesViewState(),
) {

    private val timeZone = TimeZone.currentSystemDefault()

    init {
        val date = expensesRepository.dateFlow.value
        val typeTab = getTab(tab.toIntOrNull() ?: 0)
        val tags = getTags(typeTab)
        viewState = AddExpensesViewState(
            expensesViewState = ViewState.ShowContent,
            currentCategory = TypePeriod.DAY,
            dateText = DateText(
                day = date.dayOfMonth.toString(),
                month = Dates.getMonthName(date.monthNumber),
                monthOnly = Dates.getMonthName2(date.monthNumber),
                year = date.year.toString()
            ),
            currentTabs = typeTab,
            tags = tags,
            currentTag = tags.first()
        )
    }

    override fun obtainEvent(viewEvent: AddExpensesEvent) {
        val state = viewState
        when (viewEvent) {
            is AddExpensesEvent.OnPeriodClick -> {
                changeCategory(viewEvent.category, state)
            }

            is AddExpensesEvent.OnDateChange -> {
                changeDate(viewEvent.actionDate, state)
            }

            is AddExpensesEvent.OnTabChange -> {
                changeTab(viewEvent, state)
            }

            is AddExpensesEvent.OnSumChange -> {
                viewState = state.copy(sum = viewEvent.text.toLongOrNull() ?: 0L)
            }

            is AddExpensesEvent.OnClickCategory -> {
                viewState = state.copy(currentTag = viewEvent.tag)
            }

            is AddExpensesEvent.OnCommentChanged -> {
                viewState = state.copy(comment = viewEvent.text)
            }

            is AddExpensesEvent.OnAddExpensesItem -> {
                addItem()
            }
        }
    }

    private fun changeTab(
        viewEvent: AddExpensesEvent.OnTabChange,
        state: AddExpensesViewState
    ) {
        val tags = getTags(viewEvent.typeTab)
        viewState = state.copy(
            currentTabs = viewEvent.typeTab,
            tags = tags,
            currentTag = tags.first()
        )
    }

    private fun addItem() {
        viewModelScope.launch {
            val date = expensesRepository.dateFlow.value.toInstant(timeZone).epochSeconds
            addExpenses(date, viewState.currentTabs == TypeTab.EXPENSES)
            viewAction = AddExpensesAction.ActionBack
        }
    }

    private suspend fun addExpenses(date: Long, isExpenses: Boolean) {
        expensesRepository.addItem(
            ItemDataModel(
                uuid4().mostSignificantBits,
                viewState.sum,
                viewState.comment,
                viewState.currentTag.tagName,
                date,
                isExpenses
            )
        )
    }

    private fun changeDate(actionDate: ActionDate, state: AddExpensesViewState) {
        val instant = expensesRepository.dateFlow.value.toInstant(timeZone)
        handleDate(
            actionDate, instant, state,
            when (state.currentCategory) {
                TypePeriod.DAY -> DateTimeUnit.DAY
                TypePeriod.PERIOD -> DateTimeUnit.DAY
                TypePeriod.MONTH -> DateTimeUnit.MONTH
                TypePeriod.YEAR -> DateTimeUnit.YEAR
            }
        )
    }

    private fun handleDate(
        actionDate: ActionDate,
        instant: Instant,
        state: AddExpensesViewState,
        dateTimeUnit: DateTimeUnit
    ) {
        val instantDay = when (actionDate) {
            ActionDate.INCREASE -> instant.plus(1, dateTimeUnit, timeZone)
            ActionDate.REDUCE -> instant.minus(1, dateTimeUnit, timeZone)
        }
        val localDateTimeDay = instantDay.toLocalDateTime(timeZone)
        expensesRepository.saveDate(localDateTimeDay)
        changeCategory(state.currentCategory, state)
    }

    private fun changeCategory(category: TypePeriod, state: AddExpensesViewState) {
        val date = expensesRepository.dateFlow.value

        viewState = state.copy(
            currentCategory = category,
            dateText = DateText(
                day = date.dayOfMonth.toString(),
                month = Dates.getMonthName(date.monthNumber),
                monthOnly = Dates.getMonthName2(date.monthNumber),
                year = date.year.toString()
            )
        )
    }

    private fun getTags(type: TypeTab) =
        if (type == TypeTab.EXPENSES) {
            getExpensesTags()
        } else {
            getIncomesTags()
        }
}