package screens.expenses.models

import screens.models.ActionDate
import screens.models.TypeCategory
import screens.models.TypeTab

sealed class ExpensesEvent {
    data class OnCategoryClick(val category: TypeCategory) : ExpensesEvent()
    data class OnDateChange(val actionDate: ActionDate) : ExpensesEvent()
    data class OnTabChange(val typeTab: TypeTab) : ExpensesEvent()
}