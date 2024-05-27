package screens.expenses.repository

import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDateTime

interface ExpensesRepository {

    val dateFlow: StateFlow<LocalDateTime>

    fun saveDate(date: LocalDateTime)

}