package features.expenses.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ItemDataModel(
    @PrimaryKey val id: Long,
    val sum: Long,
    val comment: String,
    val tag: String,
    val date: Long,
    val isExpenses: Boolean,
)