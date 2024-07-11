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

    @Insert(onConflict = REPLACE)
    suspend fun insert(item: IncomesDataModel)
}