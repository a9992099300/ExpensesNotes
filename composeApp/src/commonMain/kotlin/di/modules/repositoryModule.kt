package di.modules

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import features.expenses.repository.ExpensesRepository
import features.expenses.repository.ExpensesRepositoryImpl

internal val repositoryModule = DI.Module("repositoryModule") {
    bind<ExpensesRepository>() with singleton { ExpensesRepositoryImpl(instance()) }
}
