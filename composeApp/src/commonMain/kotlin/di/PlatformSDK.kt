package di

import data.database.AppDatabase
import di.database.daoModule
import features.expenses.di.repositoryModule
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.direct
import org.kodein.di.singleton

object PlatformSDK {

    fun init(
        configuration: PlatformConfiguration,
        database: AppDatabase
    ) {
        val umbrellaModule = DI.Module("umbrella") {
            bind<PlatformConfiguration>() with singleton { configuration }
            bind<AppDatabase>() with singleton { database }
        }

        Inject.createDependencies(
            DI {
                importAll(
                    umbrellaModule,
                    coreModule,
                    repositoryModule,
                    daoModule
                )
            }.direct
        )
    }
}