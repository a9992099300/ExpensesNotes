package data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import features.expenses.models.IncomesDataModel
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomesDao {

    @Query("SELECT * FROM IncomesDataModel")
    fun getAll(): Flow<List<IncomesDataModel>>

    @Query("SELECT * FROM `IncomesDataModel` WHERE date BETWEEN :since AND :until ORDER BY date DESC")
    suspend fun getPeriod(since: Long, until: Long): List<IncomesDataModel>

    @Insert(onConflict = REPLACE)
    suspend fun insert(item: IncomesDataModel)
}