package features.expenses.models

import ui.models.TypeTab

sealed class ExpensesAction {
    data class OpenAddExpenses(val date: Long, val currentTab: TypeTab) : ExpensesAction()
}