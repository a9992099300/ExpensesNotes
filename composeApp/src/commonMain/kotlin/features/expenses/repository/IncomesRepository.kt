package features.expenses.repository

import features.expenses.models.IncomesDataModel
import kotlinx.coroutines.flow.Flow

interface IncomesRepository {

    suspend fun addIncomes(model: IncomesDataModel)

    fun getIncomesList() : Flow<List<IncomesDataModel>>

}