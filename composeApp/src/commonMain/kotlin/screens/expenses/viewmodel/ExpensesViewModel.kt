package screens.expenses.viewmodel

import base.BaseViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import screens.expenses.models.ExpensesAction
import screens.expenses.models.ExpensesEvent
import screens.expenses.models.ExpensesViewState
import screens.models.ActionDate
import screens.models.TypeCategory
import screens.utils.Dates

class ExpensesViewModel : BaseViewModel<ExpensesViewState, ExpensesAction, ExpensesEvent>(
    initialState = ExpensesViewState.Content()
) {

    private var date: LocalDateTime =
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    private val timeZone = TimeZone.currentSystemDefault()
    init {
        changeCategory(TypeCategory.DAY, viewState as ExpensesViewState.Content)
    }

    override fun obtainEvent(viewEvent: ExpensesEvent) {
        when (viewEvent) {
            is ExpensesEvent.OnCategoryClick -> {
                val state = viewState
                if (state is ExpensesViewState.Content) {
                    changeCategory(viewEvent.category, state)
                }
            }
            is ExpensesEvent.OnDateChange -> {
                val state = viewState
                if (state is ExpensesViewState.Content) {
                    changeDate(viewEvent.actionDate, state)
                }
            }
        }
    }

    private fun changeDate(actionDate: ActionDate, state: ExpensesViewState.Content) {
        val instant = date.toInstant(timeZone)
        handleDate(
            actionDate, instant, state,
            when (state.currentCategory) {
                TypeCategory.DAY -> DateTimeUnit.DAY
                TypeCategory.PERIOD -> DateTimeUnit.DAY
                TypeCategory.MONTH -> DateTimeUnit.MONTH
                TypeCategory.YEAR -> DateTimeUnit.YEAR
            }
        )
    }

    private fun handleDate(
        actionDate: ActionDate,
        instant: Instant,
        state: ExpensesViewState.Content,
        dateTimeUnit: DateTimeUnit
    ) {
        val instantDay = when (actionDate) {
            ActionDate.INCREASE -> instant.plus(1, dateTimeUnit, timeZone)
            ActionDate.REDUCE -> instant.minus(1, dateTimeUnit, timeZone)
        }
        val localDateTimeDay = instantDay.toLocalDateTime(timeZone)
        date = localDateTimeDay
        changeCategory(state.currentCategory, state)
    }

    private fun changeCategory(category: TypeCategory, state: ExpensesViewState.Content) {
        val text = when (category) {
            TypeCategory.DAY -> "${date.dayOfMonth} ${Dates.getMonthName(date.monthNumber)} ${date.year}"
            TypeCategory.PERIOD -> ""
            TypeCategory.MONTH -> "${Dates.getMonthName2(date.monthNumber)} ${date.year}"
            TypeCategory.YEAR -> date.year.toString()
        }

        viewState = state.copy(
            currentCategory = category,
            dateText = text
        )
    }
}