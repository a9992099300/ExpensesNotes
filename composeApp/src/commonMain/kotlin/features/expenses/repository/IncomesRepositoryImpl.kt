package features.expenses.repository

import data.database.AppDatabase
import features.expenses.models.IncomesDataModel
import kotlinx.coroutines.flow.Flow

class IncomesRepositoryImpl(
    private val appDatabase: AppDatabase
) : IncomesRepository  {


    override suspend fun addIncomes(model: IncomesDataModel) {
        appDatabase.getIncomesDao().insert(model)
    }

    override fun getIncomesList() : Flow<List<IncomesDataModel>> =
        appDatabase.getIncomesDao().getAll()
}