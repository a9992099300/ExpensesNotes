package features.expenses.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class IncomesDataModel(
    @PrimaryKey val id: Long,
    val sum: Long,
    val comment: String,
    val tag: String,
    val date: Long
)