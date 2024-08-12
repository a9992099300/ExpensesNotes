package data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import features.expenses.models.ItemDataModel
import features.expenses.models.IncomesDataModel

@Database(entities = [ItemDataModel::class, IncomesDataModel::class], version = 2)
abstract class AppDatabase: RoomDatabase(), DB {
    abstract fun getExpensesDao(): ExpensesDao

    abstract fun getIncomesDao(): IncomesDao

    override fun clearAllTables() {
        super.clearAllTables()
    }
}

// FIXME: Added a hack to resolve below issue:
// Class 'AppDatabase_Impl' is not abstract and does not implement abstract base class member 'clearAllTables'.
interface DB {
    fun clearAllTables() {}
}

internal const val dbFileName = "app.db"