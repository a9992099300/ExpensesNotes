import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import navigation.ExpensesScreens
import navigation.LocalNavHost
import navigation.addExpension.AddExpensesFlow
import screens.main.MainScreen
import themes.MainTheme

@Composable
fun App(
    navController: NavHostController = rememberNavController()
) {
    MainTheme() {
        CompositionLocalProvider(
            LocalNavHost provides navController
        ) {
            NavHost(
                navController = navController,
                startDestination = NavigationScreens.Main.title
            ) {
                composable(route = NavigationScreens.Main.title) {
                    MainScreen()
                }
                composable(
                    route = ExpensesScreens.AddExpenses.route
                ) {
                    AddExpensesFlow()
                }
            }
        }
    }
}


enum class NavigationScreens(val title: String) {
    Main("main")
}