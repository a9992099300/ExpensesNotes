import androidx.compose.ui.window.ComposeUIViewController
import data.database.getDatabaseBuilder
import di.PlatformConfiguration
import di.PlatformSDK
import themes.MainTheme

fun MainViewController() =  ComposeUIViewController {
    val database = getDatabaseBuilder()
    PlatformSDK.init(PlatformConfiguration(), database)
    MainTheme {
        App()
    }
}