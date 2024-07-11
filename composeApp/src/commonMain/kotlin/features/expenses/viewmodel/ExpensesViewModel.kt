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
import features.expenses.models.ExpensesContentState
import features.expenses.models.ExpensesAction
import features.expenses.models.ExpensesEvent
import features.expenses.models.getDateText
import features.expenses.models.mapToExpensesUiModel
import features.expenses.models.mapToIncomesUiModel
import features.expenses.repository.ExpensesRepository
import features.expenses.repository.IncomesRepository
import features.models.ActionDate
import features.models.TypePeriod
import features.models.TypeTab
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.zip

class ExpensesViewModel(
    private val expensesRepository: ExpensesRepository = instance(),
    private val incomesRepository: IncomesRepository = instance()
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
                getExpenses()
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
                changeTab(state, viewEvent)
            }
        }
    }

    private fun changeTab(
        state: ExpensesContentState,
        viewEvent: ExpensesEvent.OnTabChange
    ) {
        viewState = state.copy(
            currentTabs = viewEvent.typeTab,
        )
        println("tab ${viewEvent.typeTab}")
        viewModelScope.launch {
            when (viewEvent.typeTab) {
                TypeTab.EXPENSES -> getExpenses()
                TypeTab.INCOMES -> getIncomes()
                TypeTab.ALL -> getAllItems()
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
            dateText = getDateText(date)
        )
    }

    private suspend fun getExpenses() {
        expensesRepository.getExpensesList().collect() {
            viewState = viewState.copy(
                items = it.map { data -> mapToExpensesUiModel(data) }
            )
        }
    }

    private suspend fun getIncomes() {
        incomesRepository.getIncomesList().collect() {
            viewState = viewState.copy(
                items = it.map { data -> mapToIncomesUiModel(data) }
            )
        }
    }

    private suspend fun getAllItems() {
        incomesRepository.getIncomesList()
            .combine(expensesRepository.getExpensesList()) { incomes, expenses ->
                val incomesUiModel = incomes.map { data -> mapToIncomesUiModel(data) }
                val expensesUiModel = expenses.map { data -> mapToExpensesUiModel(data) }
                val list = incomesUiModel + expensesUiModel
                println(list)
                viewState = viewState.copy(
                    items = list
                )
            }
    }
}