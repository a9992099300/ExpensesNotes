package features.expenses.models

import presentation.models.TypeTab

sealed class ExpensesAction {
    data class OpenAddExpenses(val date: Long, val currentTab: TypeTab) : ExpensesAction()
}