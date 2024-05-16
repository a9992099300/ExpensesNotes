import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import expensenotes.composeapp.generated.resources.Res
import expensenotes.composeapp.generated.resources.compose_multiplatform
import navigation.LocalNavHost
import screens.main.MainScreen
import screens.splash.SplashScreen
import themes.MainTheme

@Composable
fun App() {
  //  val settingsEventBus = remember { SettingsEventBus() }
 //   val currentSettings by settingsEventBus.currentSettings.collectAsState()
    MainTheme() {
        CompositionLocalProvider() {
            AppFinanceNotes()
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun AppFinanceNotes(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route ?: NavigationScreens.Splash.title

    CompositionLocalProvider(
        LocalNavHost provides navController
    ) {
        NavHost(
            navController = navController,
            startDestination = NavigationScreens.Splash.title
        ) {
            composable(route = NavigationScreens.Splash.title) {
                SplashScreen(navController)
            }
            composable(route = NavigationScreens.Splash.title) {
                MainScreen(navController)
            }
        }
    }
}

enum class NavigationScreens(val title: String) {
    Splash("splash"), Main("main")
}