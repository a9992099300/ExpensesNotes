package features.expenses.models

import expensenotes.composeapp.generated.resources.Res
import expensenotes.composeapp.generated.resources.attach_money_24dp
import expensenotes.composeapp.generated.resources.other_incomes
import expensenotes.composeapp.generated.resources.payments_24dp
import expensenotes.composeapp.generated.resources.restaurant_24dp
import expensenotes.composeapp.generated.resources.salary
import expensenotes.composeapp.generated.resources.tag_home
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource

@OptIn(ExperimentalResourceApi::class)
sealed class IncomesTag {
    open val name = Res.string.tag_home
    open val icon = Res.drawable.restaurant_24dp

    data class Salary (
        override val name: StringResource = Res.string.salary,
        override val icon: DrawableResource = Res.drawable.attach_money_24dp,
    ) : ExpensesTag()
    data class OtherIncomes(
        override val name: StringResource = Res.string.other_incomes,
        override val icon: DrawableResource = Res.drawable.payments_24dp,
    ) : ExpensesTag()

}

fun getIncomesTags() =
    listOf(IncomesTag.Salary(), IncomesTag.OtherIncomes())