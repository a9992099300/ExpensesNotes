package screens.expenses.models

import screens.models.ActionDate
import screens.models.TypeCategory

sealed class ExpensesEvent {
    data class OnCategoryClick(val category: TypeCategory) : ExpensesEvent()
    data class OnDateChange(val actionDate: ActionDate) : ExpensesEvent()
}