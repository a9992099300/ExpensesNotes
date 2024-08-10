package data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import features.expenses.models.ItemDataModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpensesDao {

    @Query("SELECT * FROM expenses")
    fun getAll(): Flow<List<ItemDataModel>>

    @Query("SELECT * FROM `expenses` WHERE date BETWEEN :since AND :until ORDER BY date DESC")
    fun getPeriod(since: Long, until: Long): Flow<List<ItemDataModel>>

    @Insert(onConflict = REPLACE)
    suspend fun insert(item: ItemDataModel)

//    @Query("SELECT * FROM ExpensesDataModel")
//    suspend fun get(id: Long) : ExpensesDataModel
}