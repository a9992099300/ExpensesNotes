package data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import features.tags.models.TagDataModel

@Dao
interface TagDao {

    @Query("SELECT * FROM tag")
   suspend fun getAll(): List<TagDataModel>

    @Insert(onConflict = REPLACE)
    suspend fun insert(item: TagDataModel)
}