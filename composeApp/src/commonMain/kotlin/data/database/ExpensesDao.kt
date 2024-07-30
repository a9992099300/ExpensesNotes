package data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import features.expenses.models.ExpensesDataModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpensesDao {

    @Query("SELECT * FROM expenses" )
   fun getAll(): Flow<List<ExpensesDataModel>>
    @Query("SELECT * FROM `expenses` WHERE date BETWEEN :since AND :until ORDER BY date DESC")
suspend fun getPeriod(since: Long, until: Long): List<ExpensesDataModel>

    @Insert(onConflict = REPLACE)
    suspend fun insert(item: ExpensesDataModel)

//    @Query("SELECT * FROM ExpensesDataModel")
//    suspend fun get(id: Long) : ExpensesDataModel
}