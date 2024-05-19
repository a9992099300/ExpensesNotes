package screens.expenses.models

sealed class ExpensesAction {
    data class OpenDetail(val itemId: String) : ExpensesAction()
}