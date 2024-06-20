package screens.expenses.addexpenses.viewmodel

import base.BaseViewModel
import di.Inject.instance
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import screens.expenses.addexpenses.models.AddExpensesAction
import screens.expenses.addexpenses.models.AddExpensesEvent
import screens.expenses.addexpenses.models.AddExpensesViewState
import screens.expenses.models.DateText
import screens.expenses.models.ViewState
import screens.expenses.models.getExpensesTags
import screens.expenses.models.getIncomesTags
import screens.expenses.repository.ExpensesRepository
import screens.models.ActionDate
import screens.models.TypePeriod
import screens.models.TypeTab
import screens.utils.Dates

class AddExpensesViewModel(
    private val expensesRepository: ExpensesRepository = instance(),
    private val date: String
) : BaseViewModel<AddExpensesViewState, AddExpensesAction, AddExpensesEvent>(
    initialState = AddExpensesViewState(),
) {

    private val timeZone = TimeZone.currentSystemDefault()

    init {
        changeCategory(
            TypePeriod.DAY,
            AddExpensesViewState(expensesViewState = ViewState.ShowContent)
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
                viewState = state.copy(currentTabs = viewEvent.typeTab)
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
        }
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