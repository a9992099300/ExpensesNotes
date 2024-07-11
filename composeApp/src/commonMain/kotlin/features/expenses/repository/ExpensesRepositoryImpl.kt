package features.expenses.repository

import data.database.AppDatabase
import features.expenses.models.ExpensesDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ExpensesRepositoryImpl(
    val appDatabase: AppDatabase
) : ExpensesRepository {

    private var _dateFlow = MutableStateFlow(
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    )

    override val dateFlow = _dateFlow.asStateFlow()

    override fun saveDate(date: LocalDateTime) {
        _dateFlow.value = date
    }

    override suspend fun addExpenses(model: ExpensesDataModel) {
        appDatabase.getExpensesDao().insert(model)
    }

    override fun getExpensesList(): Flow<List<ExpensesDataModel>> =
        appDatabase.getExpensesDao().getAll()

}