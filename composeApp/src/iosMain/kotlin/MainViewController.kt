import androidx.compose.ui.window.ComposeUIViewController
import di.PlatformConfiguration
import di.PlatformSDK
import themes.MainTheme

fun MainViewController() =  ComposeUIViewController {
    PlatformSDK.init(PlatformConfiguration())
    MainTheme {
        App()
    }
}