package navigation

sealed class ExpensesScreens(val route: String) {
    data object AddExpenses: ExpensesScreens("AddExpenses")
}