package features.expenses.viewmodel

import androidx.lifecycle.viewModelScope
import base.BaseViewModel
import di.Inject.instance
import features.expenses.models.ExpensesAction
import features.expenses.models.ExpensesContentState
import features.expenses.models.ExpensesEvent
import features.expenses.models.ItemsUiModel
import features.expenses.models.TypeData
import features.expenses.models.getDateText
import features.expenses.models.mapToExpensesUiModel
import features.expenses.models.mapToIncomesUiModel
import features.expenses.repository.ExpensesRepository
import features.expenses.repository.IncomesRepository
import features.models.ActionDate
import features.models.TypePeriod
import features.models.TypeTab
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

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
                expensesRepository.dateFlow.collectLatest { date ->
                    changeDateType(viewState.currentCategory, date)
                }
            }
        }
    }


    override fun obtainEvent(viewEvent: ExpensesEvent) {
        val state = viewState
        when (viewEvent) {
            is ExpensesEvent.OnPeriodClick -> {
                changeDateType(viewEvent.category, expensesRepository.dateFlow.value)
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
        when (viewEvent.typeTab) {
            TypeTab.EXPENSES -> getExpenses()
            TypeTab.INCOMES -> getIncomes()
            TypeTab.ALL -> getAllItems()
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
        viewModelScope.launch {
            getExpenses()
        }
    }

    private fun changeDateType(category: TypePeriod, date: LocalDateTime) {
        viewState = viewState.copy(
            currentCategory = category,
            dateText = getDateText(date)
        )
        getExpenses()
    }

    private fun getExpenses() {
        viewModelScope.launch {
            val resultList = mutableListOf<ItemsUiModel>()
            val uiModels = expensesRepository.getExpensesList(viewState.currentCategory)
                .map { data -> mapToExpensesUiModel(data) }
            uiModels.forEachIndexed { index, model ->
                if (index == 0) {
                    resultList.add(
                        ItemsUiModel(
                            id = model.id + 1000,
                            date = model.date,
                            isIncomes = false,
                            typeData = TypeData.DATE
                        )
                    )
                } else if (model.date.day != uiModels[index - 1].date.day) {
                    resultList.add(
                        ItemsUiModel(
                            id = model.id + 1000,
                            date = model.date,
                            isIncomes = false,
                            typeData = TypeData.DATE
                        )
                    )
                }
                resultList.add(model)
            }
            viewState = viewState.copy(
                items = resultList
            )
        }
    }

    private fun getIncomes() {
        viewModelScope.launch {
            incomesRepository.getIncomesList().collect() {
                viewState = viewState.copy(
                    items = it.map { data -> mapToIncomesUiModel(data) }
                )
            }
        }
    }

    private fun getAllItems() {
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