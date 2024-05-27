package screens.expenses.addexpenses.models

import screens.models.ActionDate
import screens.models.TypePeriod
import screens.models.TypeTab

sealed class AddExpensesEvent {
    data class OnPeriodClick(val category: TypePeriod) : AddExpensesEvent()
    data class OnDateChange(val actionDate: ActionDate) : AddExpensesEvent()
    data class OnTabChange(val typeTab: TypeTab) : AddExpensesEvent()
}