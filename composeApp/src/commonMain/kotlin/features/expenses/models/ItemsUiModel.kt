package features.expenses.models

import androidx.compose.runtime.Immutable
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Immutable
data class ItemsUiModel(
    val id: Long,
    val sum: Long = 0,
    val comment: String = "",
    val tag: Tag = ExpensesTag.Home(),
    val date: DateText,
    val isIncomes: Boolean,
    val typeData: TypeData
)

enum class TypeData {DATA, DATE}

fun mapToExpensesUiModel(data: ExpensesDataModel): ItemsUiModel {
    val instant = Instant.fromEpochSeconds(data.date)
    val localDateTimeDay = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return ItemsUiModel(
        id = data.id,
        sum = data.sum,
        comment = data.comment,
        tag = getExpensesTags().find { it.tagName == data.tag } ?: IncomesTag.Other(),
        date = getDateText(localDateTimeDay),
        isIncomes = false,
        typeData = TypeData.DATA
    )
}

@OptIn(ExperimentalResourceApi::class)
fun mapToIncomesUiModel(data: IncomesDataModel): ItemsUiModel {
    val instant = Instant.fromEpochSeconds(data.date / 1000)
    val localDateTimeDay = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return ItemsUiModel(
        id = data.id,
        sum = data.sum,
        comment = data.comment,
        tag = getIncomesTags().find { it.tagName == data.tag } ?: IncomesTag.Other(),
        date = getDateText(localDateTimeDay),
        isIncomes = true,
        typeData = TypeData.DATA
    )
}
