package com.example.hundredplaces.data.containers

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.hundredplaces.data.HundredPlacesLocalDatabase
import com.example.hundredplaces.data.UserPreferencesRepository
import com.example.hundredplaces.workers.WorkManagerRepository
import com.example.hundredplaces.data.model.city.CitiesRestApi
import com.example.hundredplaces.data.model.city.repositories.CitiesDataRepository
import com.example.hundredplaces.data.model.city.datasources.CitiesLocalDataSource
import com.example.hundredplaces.data.model.city.datasources.CitiesRemoteDataSource
import com.example.hundredplaces.data.model.image.ImagesRestApi
import com.example.hundredplaces.data.model.image.repositories.ImagesDataRepository
import com.example.hundredplaces.data.model.image.datasources.ImagesLocalDataSource
import com.example.hundredplaces.data.model.image.datasources.ImagesRemoteDataSource
import com.example.hundredplaces.data.model.place.PlacesRestApi
import com.example.hundredplaces.data.model.place.repositories.PlacesDataRepository
import com.example.hundredplaces.data.model.place.datasources.PlacesLocalDataSource
import com.example.hundredplaces.data.model.place.datasources.PlacesRemoteDataSource
import com.example.hundredplaces.data.model.usersPlacesPreferences.UsersPlacesPreferencesRestApi
import com.example.hundredplaces.data.model.usersPlacesPreferences.repositories.UsersPlacesPreferencesDataRepository
import com.example.hundredplaces.data.model.usersPlacesPreferences.datasources.UsersPlacesPreferencesLocalDataSource
import com.example.hundredplaces.data.model.usersPlacesPreferences.datasources.UsersPlacesPreferencesRemoteDataSource
import com.example.hundredplaces.data.model.usersPlacesPreferences.repositories.UsersPlacesPreferencesRepository
import com.example.hundredplaces.data.model.user.UsersRestApi
import com.example.hundredplaces.data.model.user.repositories.UsersDataRepository
import com.example.hundredplaces.data.model.user.datasources.UsersLocalDataSource
import com.example.hundredplaces.data.model.user.datasources.UsersRemoteDataSource
import com.example.hundredplaces.data.model.visit.VisitsRestApi
import com.example.hundredplaces.data.model.visit.repositories.VisitsDataRepository
import com.example.hundredplaces.data.model.visit.datasources.VisitsLocalDataSource
import com.example.hundredplaces.data.model.visit.datasources.VisitsRemoteDataSource
import com.example.hundredplaces.util.InstantConverter
import com.example.hundredplaces.util.NetworkConnection
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Instant

//Datastore constants
private const val USER_PREFERENCES_NAME = "user_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)
/**
 * [AppContainer] implementation that provides instances of repositories
 */
class DefaultAppContainer(
    private val context: Context
) : AppContainer{

    //Retrofit Rest constants
    private val baseUrl =
        "http://192.168.2.150:8080"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(
            GsonBuilder()
                .registerTypeAdapter(
                    Instant::class.java,
                    InstantConverter()
                ).create()))
        .build()

    private val hundredPlacesLocalDatabase by lazy {
        HundredPlacesLocalDatabase.getDatabase(context)
    }

    override val networkConnection by lazy {
        NetworkConnection(context)
    }

    override val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(context.dataStore)
    }

    override val workManagerRepository: WorkManagerRepository by lazy {
        WorkManagerRepository(context)
    }

    override val citiesRepository: CitiesDataRepository by lazy {
        CitiesDataRepository(
            CitiesLocalDataSource(hundredPlacesLocalDatabase.cityDao()),
            CitiesRemoteDataSource(
                retrofit.create(CitiesRestApi::class.java),
                networkConnection
            )
        )
    }
    override val placesRepository: PlacesDataRepository by lazy {
        PlacesDataRepository(
            PlacesLocalDataSource(hundredPlacesLocalDatabase.placeDao()),
            PlacesRemoteDataSource(
                retrofit.create(PlacesRestApi::class.java),
                networkConnection
            )
        )
    }
    override val imagesRepository: ImagesDataRepository by lazy {
        ImagesDataRepository(
            ImagesLocalDataSource(hundredPlacesLocalDatabase.imageDao()),
            ImagesRemoteDataSource(
                retrofit.create(ImagesRestApi::class.java),
                networkConnection
            )
        )
    }
    override val usersRepository: UsersDataRepository by lazy {
        UsersDataRepository(
            UsersLocalDataSource(hundredPlacesLocalDatabase.userDao()),
            UsersRemoteDataSource(retrofit.create(UsersRestApi::class.java)),
            networkConnection
        )
    }
    override val visitsRepository: VisitsDataRepository by lazy {
        VisitsDataRepository(
            VisitsLocalDataSource(hundredPlacesLocalDatabase.visitDao()),
            VisitsRemoteDataSource(
                retrofit.create(VisitsRestApi::class.java),
                networkConnection
                ),
            networkConnection
        )
    }

    override val usersPlacesPreferencesDataRepository: UsersPlacesPreferencesRepository by lazy {
        UsersPlacesPreferencesDataRepository(
            UsersPlacesPreferencesLocalDataSource(hundredPlacesLocalDatabase.usersPlacesPreferencesDao()),
            UsersPlacesPreferencesRemoteDataSource(
                retrofit.create(UsersPlacesPreferencesRestApi::class.java),
                networkConnection),
            networkConnection
        )
    }

}