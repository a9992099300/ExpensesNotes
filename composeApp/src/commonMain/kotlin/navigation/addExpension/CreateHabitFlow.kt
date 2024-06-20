package navigation.addExpension

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import screens.expenses.addexpenses.AddExpensesScreen

enum class CreateFlowScreens(val title: String) {
    Start("start")
}

@Composable
fun AddExpensesFlow(
    date: String
) {
    val navigationController = rememberNavController()

    NavHost(
        navController = navigationController,
        startDestination = CreateFlowScreens.Start.title
    ) {
        createAddExpensesFlow(date)
    }
}

fun NavGraphBuilder.createAddExpensesFlow(date: String) {
    composable(route = CreateFlowScreens.Start.title) {
        AddExpensesScreen(date)
    }
}