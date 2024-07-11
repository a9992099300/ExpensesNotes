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
sealed class IncomesTag: Tag {
    override val name = Res.string.tag_home
    override val icon = Res.drawable.restaurant_24dp
    override val tagName = "salary"

    data class Salary (
        override val name: StringResource = Res.string.salary,
        override val icon: DrawableResource = Res.drawable.attach_money_24dp,
        override val tagName: String = "salary",
    ) : ExpensesTag()
    data class OtherIncomes(
        override val name: StringResource = Res.string.other_incomes,
        override val icon: DrawableResource = Res.drawable.payments_24dp,
        override val tagName: String = "other",
    ) : ExpensesTag()

    data class Other(
        override val name: StringResource = Res.string.other_incomes,
        override val icon: DrawableResource = Res.drawable.payments_24dp,
        override val tagName: String = "other",
    ) : ExpensesTag()

}

fun getIncomesTags() =
    listOf(IncomesTag.Salary(), IncomesTag.OtherIncomes())