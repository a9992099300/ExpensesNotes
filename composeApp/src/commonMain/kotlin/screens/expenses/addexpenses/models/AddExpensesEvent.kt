package screens.expenses.addexpenses.models

import screens.expenses.models.ExpensesTag
import screens.models.ActionDate
import screens.models.TypePeriod
import screens.models.TypeTab

sealed class AddExpensesEvent {
    data class OnPeriodClick(val category: TypePeriod) : AddExpensesEvent()
    data class OnDateChange(val actionDate: ActionDate) : AddExpensesEvent()
    data class OnTabChange(val typeTab: TypeTab) : AddExpensesEvent()

    data class OnSumChange(val text: String) : AddExpensesEvent()

    data class OnClickCategory(val tag: ExpensesTag) : AddExpensesEvent()

    data class OnCommentChanged(val text: String) : AddExpensesEvent()
}