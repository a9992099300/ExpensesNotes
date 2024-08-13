package features.main

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import navigation.AppNavGraph
import navigation.MainScreens
import navigation.rememberNavigationState
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ui.themes.AppTheme

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MainScreen() {
    val navigationState = rememberNavigationState()

    val items = listOf(MainScreens.Expenses, MainScreens.Regular, MainScreens.Loans, MainScreens.Setting)
    val snackBarHostState = SnackbarHostState()
    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            BottomNavigation(backgroundColor = AppTheme.colors.navbarBackground) {

                val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()

                items.forEachIndexed { index, screen ->
                    val selected = navBackStackEntry?.destination?.hierarchy?.any {
                        it.route == screen.route
                    } ?: false
                    BottomNavigationItem(
                        icon = {
                            Icon(painter = painterResource(screen.image), contentDescription = null)
                        },
                        label = { Text(stringResource(screen.title) , color = AppTheme.colors.primaryTextColor) },
                        selected = selected,
                        onClick = {
                            if (!selected) {
                                navigationState.navigateTo(screen.route)
                            }
                        },
                        selectedContentColor = AppTheme.colors.activeBorder,
                        unselectedContentColor = AppTheme.colors.activeBorder.copy(alpha = ContentAlpha.disabled)
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) {
        AppNavGraph(navigationState)
    }
}