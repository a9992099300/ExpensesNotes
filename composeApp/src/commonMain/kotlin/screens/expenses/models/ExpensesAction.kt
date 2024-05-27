package screens.expenses.models

import kotlinx.datetime.LocalDateTime
import screens.models.TypeTab

sealed class ExpensesAction {
    data class OpenAddExpenses(val date: LocalDateTime, val currentTab: TypeTab) : ExpensesAction()
}