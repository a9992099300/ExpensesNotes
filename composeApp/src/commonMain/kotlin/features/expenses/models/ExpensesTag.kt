package features.expenses.models

import expensenotes.composeapp.generated.resources.Res
import expensenotes.composeapp.generated.resources.home_24dp
import expensenotes.composeapp.generated.resources.local_shipping_24dp
import expensenotes.composeapp.generated.resources.restaurant_24dp
import expensenotes.composeapp.generated.resources.tag_car
import expensenotes.composeapp.generated.resources.tag_food
import expensenotes.composeapp.generated.resources.tag_home
import expensenotes.composeapp.generated.resources.tag_wife
import expensenotes.composeapp.generated.resources.woman_24dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource

@OptIn(ExperimentalResourceApi::class)
sealed class ExpensesTag {
    open val name = Res.string.tag_home
    open val icon = Res.drawable.restaurant_24dp

    data class Home(
        override val name: StringResource = Res.string.tag_home,
        override val icon: DrawableResource = Res.drawable.home_24dp,
    ) : ExpensesTag()

    data class Food(
        override val name: StringResource = Res.string.tag_food,
        override val icon: DrawableResource = Res.drawable.restaurant_24dp,
    ) : ExpensesTag()

    data class Wife(
        override val name: StringResource = Res.string.tag_wife,
        override val icon: DrawableResource = Res.drawable.woman_24dp,
    ) : ExpensesTag()

    data class Car(
        override val name: StringResource = Res.string.tag_car,
        override val icon: DrawableResource = Res.drawable.local_shipping_24dp,
    ) : ExpensesTag();
}

fun getExpensesTags() =
    listOf(ExpensesTag.Home(), ExpensesTag.Food(), ExpensesTag.Wife(), ExpensesTag.Car())