package features.expenses.models

import androidx.compose.runtime.Immutable
import features.tags.models.ExpensesTag
import features.tags.models.IncomesTag
import features.tags.models.Tag
import features.tags.models.getExpensesTags
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Immutable
data class ItemsUiModel(
    val id: Long,
    val amount: Long = 0,
    val comment: String = "",
    val tag: Tag = ExpensesTag.Home(),
    val date: DateText,
    val isExpenses: Boolean,
    val typeData: TypeData
)

enum class TypeData {DATA, DATE}

fun mapToExpensesUiModel(data: ItemDataModel): ItemsUiModel {
    val instant = Instant.fromEpochSeconds(data.date)
    val localDateTimeDay = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return ItemsUiModel(
        id = data.id,
        amount = data.sum,
        comment = data.comment,
        tag = getExpensesTags().find { it.tagName == data.tag } ?: IncomesTag.Other(),
        date = getDateText(localDateTimeDay),
        isExpenses = data.isExpenses,
        typeData = TypeData.DATA
    )
}

