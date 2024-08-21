package features.expenses.viewmodel

import androidx.lifecycle.viewModelScope
import presentation.base.BaseViewModel
import di.Inject.instance
import features.expenses.models.ExpensesAction
import features.expenses.models.ExpensesContentState
import features.expenses.models.ExpensesEvent
import features.expenses.models.ItemsUiModel
import features.expenses.models.TypeData
import features.expenses.models.getDateText
import features.expenses.models.mapToExpensesUiModel
import features.expenses.repository.ExpensesRepository
import presentation.models.ActionDate
import presentation.models.TypePeriod
import presentation.models.TypeTab
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.collectLatest
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
) : BaseViewModel<ExpensesContentState, ExpensesAction, ExpensesEvent>(
    initialState = ExpensesContentState(),
) {

    private val timeZone = TimeZone.currentSystemDefault()
    private val job = Job()

    init {
        viewModelScope.launch {
            expensesRepository.dateFlow.collectLatest { date ->
                changeDateType(viewState.currentCategory, date)
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
            ExpensesEvent.OnAddClick -> {
                viewModelScope.launch {
                    viewAction = ExpensesAction.OpenAddExpenses(0, viewState.currentTabs)
                }
            }
            is ExpensesEvent.OnDeleteItem -> {
                viewModelScope.launch {
                    expensesRepository.deleteItem(viewEvent.id)
                }
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
        when (viewEvent.typeTab) {
            TypeTab.EXPENSES -> getItems(true)
            TypeTab.INCOMES -> getItems(false)
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
    }

    private fun changeDateType(category: TypePeriod, date: LocalDateTime) {
        viewState = viewState.copy(
            currentCategory = category,
            dateText = getDateText(date)
        )

        when (viewState.currentTabs) {
            TypeTab.EXPENSES -> getItems(true)
            TypeTab.INCOMES -> getItems(false)
            TypeTab.ALL -> getAllItems()
        }
    }

    private fun getItems(isExpenses: Boolean) {
        job.cancelChildren()
        viewModelScope.launch(job) {
                expensesRepository.getItemsList(viewState.currentCategory).collectLatest {
                    val models = it
                        .filter { it.isExpenses == isExpenses }
                        .map { data -> mapToExpensesUiModel(data) }
                    handleItems(models)
                }
        }
    }

    private fun getAllItems() {
        job.cancelChildren()
        viewModelScope.launch(job) {
            expensesRepository.getItemsList(viewState.currentCategory).collectLatest {
                val models = it
                    .map { data -> mapToExpensesUiModel(data) }
                handleItems(models)
            }
        }
    }

    private fun handleItems(uiModels: List<ItemsUiModel>) {
        val resultList = mutableListOf<ItemsUiModel>()
        uiModels.forEachIndexed { index, model ->
            if (viewState.currentCategory != TypePeriod.DAY) {
                if (index == 0) {
                    resultList.add(
                        ItemsUiModel(
                            id = model.id + 1000,
                            date = model.date,
                            isExpenses = model.isExpenses,
                            typeData = TypeData.DATE
                        )
                    )
                } else if (
                    model.date.day != uiModels[index - 1].date.day ||
                    model.date.month != uiModels[index - 1].date.month
                ) {
                    resultList.add(
                        ItemsUiModel(
                            id = model.id + 1000,
                            date = model.date,
                            isExpenses = model.isExpenses,
                            typeData = TypeData.DATE
                        )
                    )
                }
            }
            resultList.add(model)
        }

        viewState = viewState.copy(
            items = resultList,
            expensesSum = resultList.filter { it.isExpenses }.sumOf { it.amount },
            incomesSum = resultList.filter { !it.isExpenses }.sumOf { it.amount },
        )
    }
}