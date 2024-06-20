package features.expenses.models

import features.models.ActionDate
import features.models.TypePeriod
import features.models.TypeTab

sealed class ExpensesEvent {
    data class OnPeriodClick(val category: TypePeriod) : ExpensesEvent()
    data class OnDateChange(val actionDate: ActionDate) : ExpensesEvent()
    data class OnTabChange(val typeTab: TypeTab) : ExpensesEvent()

}