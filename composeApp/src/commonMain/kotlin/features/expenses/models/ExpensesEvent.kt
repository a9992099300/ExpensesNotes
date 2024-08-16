package features.expenses.models

import presentation.models.ActionDate
import presentation.models.TypePeriod
import presentation.models.TypeTab

sealed class ExpensesEvent {
    data class OnPeriodClick(val category: TypePeriod) : ExpensesEvent()
    data class OnDateChange(val actionDate: ActionDate) : ExpensesEvent()
    data class OnTabChange(val typeTab: TypeTab) : ExpensesEvent()
    data object OnAddClick : ExpensesEvent()
    data class OnDeleteItem(val id: Long) : ExpensesEvent()
}