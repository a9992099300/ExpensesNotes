package features.expenses.addexpenses.viewmodel

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
import features.expenses.addexpenses.models.AddExpensesAction
import features.expenses.addexpenses.models.AddExpensesEvent
import features.expenses.addexpenses.models.AddExpensesViewState
import features.expenses.models.DateText
import features.expenses.models.ExpensesDataModel
import features.expenses.models.IncomesDataModel
import features.expenses.models.ViewState
import features.expenses.models.getExpensesTags
import features.expenses.models.getIncomesTags
import features.expenses.repository.ExpensesRepository
import features.expenses.repository.IncomesRepository
import features.models.ActionDate
import features.models.TypePeriod
import features.models.TypeTab
import features.utils.Dates
import kotlinx.coroutines.launch

class AddExpensesViewModel(
    private val expensesRepository: ExpensesRepository = instance(),
    private val incomesRepository: IncomesRepository = instance(),
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
                val tags = getTags(viewEvent.typeTab)
                viewState = state.copy(
                    currentTabs = viewEvent.typeTab,
                    tags = tags,
                    currentTag = tags.first()
                )
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

    private fun addItem() {
        viewModelScope.launch {
            val date = expensesRepository.dateFlow.value.toInstant(timeZone).epochSeconds
            if(viewState.currentTabs == TypeTab.EXPENSES) {
                addExpenses(date)
            } else {
                addIncomes(date)
            }
            expensesRepository.resetDate(expensesRepository.dateFlow.value)
            viewAction = AddExpensesAction.ActionBack
        }
    }
    private suspend fun addExpenses(date: Long) {
        expensesRepository.addExpenses(
            ExpensesDataModel(
                uuid4().mostSignificantBits,
                viewState.sum,
                viewState.comment,
                viewState.currentTag.tagName,
                date
            )
        )
    }

    private suspend fun addIncomes(date: Long) {
        incomesRepository.addIncomes(
            IncomesDataModel(
                uuid4().mostSignificantBits,
                viewState.sum,
                viewState.comment,
                viewState.currentTag.tagName,
                date
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