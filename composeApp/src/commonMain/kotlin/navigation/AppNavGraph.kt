package navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import screens.expenses.ExpensesScreen
import screens.incomes.IncomesScreen

@Composable
fun AppNavGraph(
    navigationState: NavigationState,
) {
    NavHost(navigationState.navHostController, startDestination = MainScreens.Expenses.route) {
        composable(MainScreens.Expenses.route) { ExpensesScreen() }
        composable(MainScreens.Regular.route) { }
        composable(MainScreens.Loans.route) { }
    }
}