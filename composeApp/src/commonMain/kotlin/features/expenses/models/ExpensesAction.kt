package features.expenses.models

import features.models.TypeTab

sealed class ExpensesAction {
    data class OpenAddExpenses(val date: Long, val currentTab: TypeTab) : ExpensesAction()
}