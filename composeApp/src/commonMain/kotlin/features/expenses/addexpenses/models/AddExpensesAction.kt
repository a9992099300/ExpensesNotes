package features.expenses.addexpenses.models

sealed class AddExpensesAction {
    data object ActionBack : AddExpensesAction()
}