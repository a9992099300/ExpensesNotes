package screens.expenses.models

import screens.models.ActionDate
import screens.models.TypePeriod
import screens.models.TypeTab

sealed class ExpensesEvent {
    data class OnPeriodClick(val category: TypePeriod) : ExpensesEvent()
    data class OnDateChange(val actionDate: ActionDate) : ExpensesEvent()
    data class OnTabChange(val typeTab: TypeTab) : ExpensesEvent()

}