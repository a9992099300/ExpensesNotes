package navigation.addExpension

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import features.expenses.addexpenses.AddExpensesScreen

enum class CreateFlowScreens(val title: String) {
    Start("start")
}

@Composable
fun AddExpensesFlow(
    tab: String
) {
    val navigationController = rememberNavController()

    NavHost(
        navController = navigationController,
        startDestination = CreateFlowScreens.Start.title
    ) {
        createAddExpensesFlow(tab)
    }
}

fun NavGraphBuilder.createAddExpensesFlow(tab: String) {
    composable(route = CreateFlowScreens.Start.title) {
        AddExpensesScreen(tab)
    }
}