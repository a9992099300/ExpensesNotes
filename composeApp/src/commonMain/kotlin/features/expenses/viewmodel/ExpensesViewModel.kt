package features.expenses.viewmodel

import androidx.lifecycle.viewModelScope
import base.BaseViewModel
import di.Inject.instance
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import features.expenses.models.DateText
import features.expenses.models.ExpensesContentState
import features.expenses.models.ExpensesAction
import features.expenses.models.ExpensesEvent
import features.expenses.repository.ExpensesRepository
import features.models.ActionDate
import features.models.TypePeriod
import features.utils.Dates

class ExpensesViewModel(
    private val expensesRepository: ExpensesRepository = instance()
) : BaseViewModel<ExpensesContentState, ExpensesAction, ExpensesEvent>(
    initialState = ExpensesContentState(),
) {

    private val timeZone = TimeZone.currentSystemDefault()

    init {
        viewModelScope.launch {
            launch {
                expensesRepository.dateFlow.collectLatest {
                    changeDateType(viewState.currentCategory)
                }
            }
            launch {
                expensesRepository.getExpensesList().collect() {
                    viewState = viewState.copy(
                        expensesItems = it
                    )
                }
            }
        }
    }

    override fun obtainEvent(viewEvent: ExpensesEvent) {
        val state = viewState
        when (viewEvent) {
            is ExpensesEvent.OnPeriodClick -> {
                changeDateType(viewEvent.category)
            }
            is ExpensesEvent.OnDateChange -> {
                changeDate(viewEvent.actionDate, state)
            }
            is ExpensesEvent.OnTabChange -> {
                viewState = state.copy(
                    currentTabs = viewEvent.typeTab,
                )
            }
        }
    }

    private fun changeDate(actionDate: ActionDate, state: ExpensesContentState) {
        val instant = expensesRepository.dateFlow.value.toInstant(timeZone)
        handleDate(
            actionDate, instant,
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
        dateTimeUnit: DateTimeUnit
    ) {
        val instantDay = when (actionDate) {
            ActionDate.INCREASE -> instant.plus(1, dateTimeUnit, timeZone)
            ActionDate.REDUCE -> instant.minus(1, dateTimeUnit, timeZone)
        }
        val localDateTimeDay = instantDay.toLocalDateTime(timeZone)
        expensesRepository.saveDate(localDateTimeDay)
    }

    private fun changeDateType(category: TypePeriod) {
        val date = expensesRepository.dateFlow.value

        viewState = viewState.copy(
            currentCategory = category,
            dateText = DateText(
                day = date.dayOfMonth.toString(),
                month = Dates.getMonthName(date.monthNumber),
                year = date.year.toString()
            )
        )
    }
}