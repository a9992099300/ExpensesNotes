package features.addexpenses.models

sealed class AddExpensesAction {
    data object ActionBack : AddExpensesAction()
}