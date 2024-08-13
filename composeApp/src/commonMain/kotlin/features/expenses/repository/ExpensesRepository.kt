package features.expenses.repository

import features.expenses.models.ItemDataModel
import ui.models.TypePeriod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDateTime

interface ExpensesRepository {

    val dateFlow: StateFlow<LocalDateTime>

    fun saveDate(date: LocalDateTime)

    fun resetDate(date: LocalDateTime)

    suspend fun addItem(model: ItemDataModel)

    fun getItemsList(): Flow<List<ItemDataModel>>

    suspend fun getItemsList(typePeriod: TypePeriod): Flow<List<ItemDataModel>>

}