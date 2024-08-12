package navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import expensenotes.composeapp.generated.resources.Res
import expensenotes.composeapp.generated.resources.attach_money_24dp
import expensenotes.composeapp.generated.resources.expenses_incomes
import expensenotes.composeapp.generated.resources.home_24dp
import expensenotes.composeapp.generated.resources.loans
import expensenotes.composeapp.generated.resources.payments_24dp
import expensenotes.composeapp.generated.resources.regular_payment
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource

@OptIn(ExperimentalResourceApi::class)
sealed class MainScreens(val route: String, val title: StringResource, val image: DrawableResource) {
    @OptIn(ExperimentalResourceApi::class)
    data object Expenses :
        MainScreens("expenses", Res.string.expenses_incomes, Res.drawable.home_24dp)

    //    data object Incomes : MainScreens("incomes", "Доходы", Icons.Outlined.Check)
    @OptIn(ExperimentalResourceApi::class)
    data object Regular : MainScreens("regular", Res.string.regular_payment, Res.drawable.payments_24dp)

    @OptIn(ExperimentalResourceApi::class)
    data object Loans : MainScreens("loans", Res.string.loans, Res.drawable.attach_money_24dp)
}