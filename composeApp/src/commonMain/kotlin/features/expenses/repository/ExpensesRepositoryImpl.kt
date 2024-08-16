package features.expenses.repository

import data.database.AppDatabase
import features.expenses.models.ItemDataModel
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
import presentation.models.TypePeriod

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

    override suspend fun addItem(model: ItemDataModel) {
        appDatabase.getExpensesDao().insert(model)
    }

    override fun getItemsList(): Flow<List<ItemDataModel>> =
        appDatabase.getExpensesDao().getAll()

    override suspend fun getItemsList(
        typePeriod: TypePeriod
    ): Flow<List<ItemDataModel>> =
        when (typePeriod) {
            TypePeriod.DAY -> getExpensesListDay()
            TypePeriod.PERIOD -> getExpensesListDay()
            TypePeriod.MONTH -> getExpensesListMonth()
            TypePeriod.YEAR -> getExpensesListYears()
        }

    override suspend fun deleteItem(id: Long) {
        appDatabase.getExpensesDao().deleteItem(id)
    }

    private fun getExpensesListDay(): Flow<List<ItemDataModel>> {
        val dayStart = dateFlow.value.date.atStartOfDayIn(timeZone)
        val dayEnd = dayStart.plus(1, DateTimeUnit.DAY, timeZone).epochSeconds
        return appDatabase.getExpensesDao().getPeriod(dayStart.epochSeconds, dayEnd)
    }

    private fun getExpensesListMonth(): Flow<List<ItemDataModel>> {
        val date = dateFlow.value.date
        val startMonth = LocalDateTime(date.year, date.month, 1, 0, 0, 0).toInstant(timeZone)
        val endMonth = startMonth.plus(1, DateTimeUnit.MONTH, timeZone)
        return appDatabase.getExpensesDao()
            .getPeriod(startMonth.epochSeconds, endMonth.epochSeconds)
    }

    private fun getExpensesListYears(): Flow<List<ItemDataModel>> {
        val date = dateFlow.value.date
        val startYears = LocalDateTime(date.year, 1, 1, 0, 0, 0).toInstant(timeZone)
        val endYears = startYears.plus(1, DateTimeUnit.YEAR, timeZone)
        return appDatabase.getExpensesDao()
            .getPeriod(startYears.epochSeconds, endYears.epochSeconds)
    }

}