package features.addexpenses.models

import features.tags.models.ExpensesTag
import presentation.models.ActionDate
import presentation.models.TypePeriod
import presentation.models.TypeTab

sealed class AddExpensesEvent {
    data class OnPeriodClick(val category: TypePeriod) : AddExpensesEvent()
    data class OnDateChange(val actionDate: ActionDate) : AddExpensesEvent()
    data class OnTabChange(val typeTab: TypeTab) : AddExpensesEvent()

    data class OnSumChange(val text: String) : AddExpensesEvent()

    data class OnClickCategory(val tag: ExpensesTag) : AddExpensesEvent()

    data class OnCommentChanged(val text: String) : AddExpensesEvent()

    data object OnAddExpensesItem : AddExpensesEvent()
}