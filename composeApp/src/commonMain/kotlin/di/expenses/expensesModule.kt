package di.expenses

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton
import screens.expenses.repository.ExpensesRepository
import screens.expenses.repository.ExpensesRepositoryImpl

internal val expensesModule = DI.Module("expensesModule") {
        bind <ExpensesRepository>() with singleton  { ExpensesRepositoryImpl() }
    }
