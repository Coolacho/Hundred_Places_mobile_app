package com.example.hundredplaces.data.containers

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.hundredplaces.data.HundredPlacesLocalDatabase
import com.example.hundredplaces.data.UserPreferencesRepository
import com.example.hundredplaces.data.WorkManagerRepository
import com.example.hundredplaces.data.model.city.CityRestApiService
import com.example.hundredplaces.data.model.city.repositories.CitiesDataRepository
import com.example.hundredplaces.data.model.city.repositories.CitiesLocalRepository
import com.example.hundredplaces.data.model.city.repositories.CitiesRemoteRepository
import com.example.hundredplaces.data.model.image.ImageRestApiService
import com.example.hundredplaces.data.model.image.repositories.ImagesDataRepository
import com.example.hundredplaces.data.model.image.repositories.ImagesLocalRepository
import com.example.hundredplaces.data.model.image.repositories.ImagesRemoteRepository
import com.example.hundredplaces.data.model.place.PlaceRestApiService
import com.example.hundredplaces.data.model.place.repositories.PlacesDataRepository
import com.example.hundredplaces.data.model.place.repositories.PlacesLocalRepository
import com.example.hundredplaces.data.model.place.repositories.PlacesRemoteRepository
import com.example.hundredplaces.data.model.user.UserRestApiService
import com.example.hundredplaces.data.model.user.repositories.UsersDataRepository
import com.example.hundredplaces.data.model.user.repositories.UsersLocalRepository
import com.example.hundredplaces.data.model.user.repositories.UsersRemoteRepository
import com.example.hundredplaces.data.model.visit.VisitRestApiService
import com.example.hundredplaces.data.model.visit.repositories.VisitsDataRepository
import com.example.hundredplaces.data.model.visit.repositories.VisitsLocalRepository
import com.example.hundredplaces.data.model.visit.repositories.VisitsRemoteRepository
import com.example.hundredplaces.util.LocalDateTimeConverter
import com.example.hundredplaces.util.NetworkConnection
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

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
        "http://192.168.2.150:8080/api/v1/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(
            GsonBuilder()
                .registerTypeAdapter(
                    LocalDateTime::class.java,
                    LocalDateTimeConverter()
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
            CitiesLocalRepository(hundredPlacesLocalDatabase.cityDao()),
            CitiesRemoteRepository(retrofit.create(CityRestApiService::class.java)),
            networkConnection
        )
    }
    override val placesRepository: PlacesDataRepository by lazy {
        PlacesDataRepository(
            PlacesLocalRepository(hundredPlacesLocalDatabase.placeDao()),
            PlacesRemoteRepository(retrofit.create(PlaceRestApiService::class.java)),
            networkConnection
        )
    }
    override val imagesRepository: ImagesDataRepository by lazy {
        ImagesDataRepository(
            ImagesLocalRepository(hundredPlacesLocalDatabase.imageDao()),
            ImagesRemoteRepository(retrofit.create(ImageRestApiService::class.java)),
            networkConnection
        )
    }
    override val usersRepository: UsersDataRepository by lazy {
        UsersDataRepository(
            UsersLocalRepository(hundredPlacesLocalDatabase.userDao()),
            UsersRemoteRepository(retrofit.create(UserRestApiService::class.java)),
            networkConnection
        )
    }
    override val visitsRepository: VisitsDataRepository by lazy {
        VisitsDataRepository(
            VisitsLocalRepository(hundredPlacesLocalDatabase.visitDao()),
            VisitsRemoteRepository(retrofit.create(VisitRestApiService::class.java)),
            networkConnection
        )
    }
}