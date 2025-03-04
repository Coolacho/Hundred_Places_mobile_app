package com.example.hundredplaces

import android.app.Application
import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import com.example.hundredplaces.data.containers.DefaultAppContainer
import com.example.hundredplaces.workers.CustomWorkerFactory

class HundredPlacesApplication : Application(), Configuration.Provider {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: DefaultAppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }

    override val workManagerConfiguration: Configuration
        get() {
            val customWorkerFactory = DelegatingWorkerFactory()
            customWorkerFactory.addFactory(
                CustomWorkerFactory(
                    container.citiesRepository,
                    container.placesRepository,
                    container.imagesRepository,
                    container.usersPlacesPreferencesDataRepository,
                    container.visitsRepository
                )
            )

            return Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.INFO)
                .setWorkerFactory(customWorkerFactory)
                .build()
        }
}