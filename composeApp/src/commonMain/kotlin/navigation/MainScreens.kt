package navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class MainScreens(val route: String, val title: String, val image: ImageVector) {
    data object Expenses : MainScreens("expenses", "Расходы", Icons.AutoMirrored.Filled.List)
    data object Incomes : MainScreens("incomes", "Доходы", Icons.Outlined.Check)
    data object Regular : MainScreens("regular", "Регулярные", Icons.Outlined.Settings)
    data object Loans : MainScreens("loans", "Кредиты", Icons.Outlined.Settings)
}