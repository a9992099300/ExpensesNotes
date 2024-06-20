import NavigationScreens.Companion.KEY_DATE
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
                    route = NavigationScreens.AddExpenses.title,
                    arguments = listOf(
                        navArgument(KEY_DATE) {
                            type = NavType.StringType
                        },
                    )
                ) {
                    val date = it.arguments?.getString(KEY_DATE) ?: throw RuntimeException("Args is null")
                    AddExpensesFlow(date)
                }
            }
        }
    }
}


sealed class NavigationScreens(val title: String) {

    companion object{
        const val KEY_DATE = "date"
        const val ROUTE_ADD_EXPENSES_ARGS = "AddExpenses/{$KEY_DATE}"
        private const val ROUTE_ADD_EXPENSES = "AddExpenses"
    }
   data object Main : NavigationScreens("main")

    data object AddExpenses: NavigationScreens(ROUTE_ADD_EXPENSES_ARGS) {
        fun getRouteWithArgs(date: String) : String {
            return "$ROUTE_ADD_EXPENSES/${date}"
        }
    }
}