package screens.expenses.viewmodel

import base.BaseViewModel
import di.Inject.instance
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import screens.expenses.models.ExpensesContentState
import screens.expenses.models.ExpensesAction
import screens.expenses.models.ExpensesEvent
import screens.expenses.models.ViewState
import screens.expenses.models.getExpensesTags
import screens.expenses.models.getIncomesTags
import screens.expenses.repository.ExpensesRepository
import screens.models.ActionDate
import screens.models.TypePeriod
import screens.models.TypeTab
import screens.utils.Dates

class ExpensesViewModel(
    private val expensesRepository: ExpensesRepository = instance()
) : BaseViewModel<ExpensesContentState, ExpensesAction, ExpensesEvent>(
    initialState = ExpensesContentState(),
) {

    private val timeZone = TimeZone.currentSystemDefault()

    init {
        changeCategory(
            TypePeriod.DAY,
            ExpensesContentState(viewState = ViewState.ShowContent)
        )
    }

    override fun obtainEvent(viewEvent: ExpensesEvent) {
        val state = viewState
        when (viewEvent) {
            is ExpensesEvent.OnPeriodClick -> {
                changeCategory(viewEvent.category, state)
            }

            is ExpensesEvent.OnDateChange -> {
                changeDate(viewEvent.actionDate, state)
            }

            is ExpensesEvent.OnTabChange -> {
                val tabs = getTags(viewEvent.typeTab)
                viewState = state.copy(
                    currentTabs = viewEvent.typeTab,
                    tags = tabs,
                    currentTag = tabs.first()
                )
            }

            is ExpensesEvent.OnStateScreenChange -> {
                val tabs = getTags(TypeTab.EXPENSES)
                viewState = state.copy(
                    stateScreen = viewEvent.stateScreen,
                    currentTabs = TypeTab.EXPENSES,
                    tags = tabs,
                    currentTag = tabs.first()
                )
            }

            is ExpensesEvent.OnSumChange -> {
                viewState = state.copy(sum = viewEvent.text.toLongOrNull() ?: 0L)
            }

            is ExpensesEvent.OnClickCategory -> {
                viewState = state.copy(currentTag = viewEvent.tag)
            }

            is ExpensesEvent.OnCommentChanged -> {
                viewState = state.copy(comment = viewEvent.text)
            }
        }
    }

    private fun changeDate(actionDate: ActionDate, state: ExpensesContentState) {
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
        state: ExpensesContentState,
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

    private fun changeCategory(category: TypePeriod, state: ExpensesContentState) {
        val date = expensesRepository.dateFlow.value
        val text = when (category) {
            TypePeriod.DAY -> "${date.dayOfMonth} ${Dates.getMonthName(date.monthNumber)} ${date.year}"
            TypePeriod.PERIOD -> ""
            TypePeriod.MONTH -> "${Dates.getMonthName2(date.monthNumber)} ${date.year}"
            TypePeriod.YEAR -> date.year.toString()
        }

        viewState = state.copy(
            currentCategory = category,
            dateText = text
        )
    }

    private fun getTags(type: TypeTab) =
        if (type == TypeTab.EXPENSES) {
            getExpensesTags()
        } else {
            getIncomesTags()
        }
}