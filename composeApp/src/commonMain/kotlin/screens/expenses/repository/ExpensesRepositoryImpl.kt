package screens.expenses.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ExpensesRepositoryImpl() : ExpensesRepository {

    private var _dateFlow = MutableStateFlow(
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    )


    override val dateFlow = _dateFlow.asStateFlow()

    override fun saveDate(date: LocalDateTime) {
        _dateFlow.value = date
    }

}