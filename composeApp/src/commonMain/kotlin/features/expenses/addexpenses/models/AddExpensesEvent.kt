package features.expenses.addexpenses.models

import features.expenses.models.ExpensesTag
import features.models.ActionDate
import features.models.TypePeriod
import features.models.TypeTab

sealed class AddExpensesEvent {
    data class OnPeriodClick(val category: TypePeriod) : AddExpensesEvent()
    data class OnDateChange(val actionDate: ActionDate) : AddExpensesEvent()
    data class OnTabChange(val typeTab: TypeTab) : AddExpensesEvent()

    data class OnSumChange(val text: String) : AddExpensesEvent()

    data class OnClickCategory(val tag: ExpensesTag) : AddExpensesEvent()

    data class OnCommentChanged(val text: String) : AddExpensesEvent()

    data object OnAddExpensesItem : AddExpensesEvent()
}