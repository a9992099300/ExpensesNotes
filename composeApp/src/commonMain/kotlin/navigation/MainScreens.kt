package navigation

import expensenotes.composeapp.generated.resources.Res
import expensenotes.composeapp.generated.resources.attach_money_24dp
import expensenotes.composeapp.generated.resources.expenses_incomes
import expensenotes.composeapp.generated.resources.home_24dp
import expensenotes.composeapp.generated.resources.loans
import expensenotes.composeapp.generated.resources.payments_24dp
import expensenotes.composeapp.generated.resources.regular_payment
import expensenotes.composeapp.generated.resources.setting
import expensenotes.composeapp.generated.resources.settings_24dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource

@OptIn(ExperimentalResourceApi::class)
sealed class MainScreens(val route: String, val title: StringResource, val image: DrawableResource) {
    @OptIn(ExperimentalResourceApi::class)
    data object Expenses :
        MainScreens("expenses", Res.string.expenses_incomes, Res.drawable.home_24dp)

    @OptIn(ExperimentalResourceApi::class)
    data object Regular : MainScreens("regular", Res.string.regular_payment, Res.drawable.payments_24dp)

    @OptIn(ExperimentalResourceApi::class)
    data object Loans : MainScreens("loans", Res.string.loans, Res.drawable.attach_money_24dp)

    @OptIn(ExperimentalResourceApi::class)
    data object Setting : MainScreens("setting", Res.string.setting, Res.drawable.settings_24dp)
}