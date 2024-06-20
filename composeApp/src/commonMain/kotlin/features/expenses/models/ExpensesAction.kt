package features.expenses.models

import kotlinx.datetime.LocalDateTime
import features.models.TypeTab

sealed class ExpensesAction {
    data class OpenAddExpenses(val date: LocalDateTime, val currentTab: TypeTab) : ExpensesAction()
}