package features.expenses.repository

import features.expenses.models.ExpensesDataModel
import features.models.TypePeriod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDateTime

interface ExpensesRepository {

    val dateFlow: StateFlow<LocalDateTime>

    fun saveDate(date: LocalDateTime)

    fun resetDate(date: LocalDateTime)

    suspend fun addExpenses(model: ExpensesDataModel)

    fun getExpensesList(): Flow<List<ExpensesDataModel>>

    suspend fun getExpensesList(typePeriod: TypePeriod): List<ExpensesDataModel>

}