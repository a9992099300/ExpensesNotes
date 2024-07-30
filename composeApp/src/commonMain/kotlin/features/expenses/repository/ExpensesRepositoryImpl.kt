package features.expenses.repository

import data.database.AppDatabase
import features.expenses.models.ExpensesDataModel
import features.models.TypePeriod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class ExpensesRepositoryImpl(
    private val appDatabase: AppDatabase
) : ExpensesRepository {

    private val timeZone = TimeZone.currentSystemDefault()

    private var _dateFlow = MutableStateFlow(
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    )

    override val dateFlow = _dateFlow.asStateFlow()

    override fun saveDate(date: LocalDateTime) {
        _dateFlow.value = date
    }

    override fun resetDate(date: LocalDateTime) {
        _dateFlow.value = LocalDateTime(0, 1, 1, 0, 0, 0, 0)
        _dateFlow.value = date
    }

    override suspend fun addExpenses(model: ExpensesDataModel) {
        appDatabase.getExpensesDao().insert(model)
    }

    override fun getExpensesList(): Flow<List<ExpensesDataModel>> =
        appDatabase.getExpensesDao().getAll()

    override suspend fun getExpensesList(typePeriod: TypePeriod): List<ExpensesDataModel> =
        when (typePeriod) {
            TypePeriod.DAY -> getExpensesListDay()
            TypePeriod.PERIOD -> getExpensesListDay()
            TypePeriod.MONTH -> getExpensesListMonth()
            TypePeriod.YEAR -> getExpensesListYears()
        }

    private suspend fun getExpensesListDay(): List<ExpensesDataModel> {
        val dayStart = dateFlow.value.date.atStartOfDayIn(timeZone)
        val dayEnd = dayStart.plus(1, DateTimeUnit.DAY, timeZone).epochSeconds
        return appDatabase.getExpensesDao().getPeriod(dayStart.epochSeconds, dayEnd)
    }

    private suspend fun getExpensesListMonth(): List<ExpensesDataModel> {
        val date = dateFlow.value.date
        val startMonth = LocalDateTime(date.year, date.month, 1, 0, 0, 0).toInstant(timeZone)
        val endMonth = startMonth.plus(1, DateTimeUnit.MONTH, timeZone)
        return appDatabase.getExpensesDao()
            .getPeriod(startMonth.epochSeconds, endMonth.epochSeconds)
    }

    private suspend fun getExpensesListYears(): List<ExpensesDataModel> {
        val date = dateFlow.value.date
        val startYears = LocalDateTime(date.year, 1, 1, 0, 0, 0).toInstant(timeZone)
        val endYears = startYears.plus(1, DateTimeUnit.YEAR, timeZone)
        return appDatabase.getExpensesDao()
            .getPeriod(startYears.epochSeconds, endYears.epochSeconds)
    }

}