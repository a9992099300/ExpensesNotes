package features.expenses.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import features.expenses.repository.ExpensesRepository
import features.expenses.repository.ExpensesRepositoryImpl
import features.expenses.repository.IncomesRepository
import features.expenses.repository.IncomesRepositoryImpl

internal val repositoryModule = DI.Module("repositoryModule") {
    bind<ExpensesRepository>() with singleton { ExpensesRepositoryImpl(instance()) }
    bind<IncomesRepository>() with singleton { IncomesRepositoryImpl(instance()) }
}
