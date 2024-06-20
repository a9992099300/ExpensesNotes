package di.database

import data.database.AppDatabase
import data.database.ExpensesDao
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

internal val daoModule = DI.Module("daoModule") {
    bind<ExpensesDao>() with singleton { instance<AppDatabase>().getExpensesDao() }
}
