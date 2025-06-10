package com.example.hundredplaces.data.containers

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.hundredplaces.data.HundredPlacesLocalDatabase
import com.example.hundredplaces.data.UserAppPreferencesRepository
import com.example.hundredplaces.data.model.city.datasources.CityRestApi
import com.example.hundredplaces.data.model.city.repositories.CityRepository
import com.example.hundredplaces.data.model.city.repositories.DefaultCityRepository
import com.example.hundredplaces.data.model.image.datasources.ImageRestApi
import com.example.hundredplaces.data.model.image.repositories.DefaultImageRepository
import com.example.hundredplaces.data.model.image.repositories.ImageRepository
import com.example.hundredplaces.data.model.place.datasources.PlaceRestApi
import com.example.hundredplaces.data.model.place.repositories.DefaultPlaceRepository
import com.example.hundredplaces.data.model.place.repositories.PlaceRepository
import com.example.hundredplaces.data.model.user.datasources.UserRestApi
import com.example.hundredplaces.data.model.user.repositories.DefaultUserRepository
import com.example.hundredplaces.data.model.user.repositories.UserRepository
import com.example.hundredplaces.data.model.usersPlacesPreferences.datasources.UsersPlacesPreferencesRestApi
import com.example.hundredplaces.data.model.usersPlacesPreferences.repositories.DefaultUsersPlacesPreferencesRepository
import com.example.hundredplaces.data.model.usersPlacesPreferences.repositories.UsersPlacesPreferencesRepository
import com.example.hundredplaces.data.model.visit.datasources.VisitRestApi
import com.example.hundredplaces.data.model.visit.repositories.DefaultVisitRepository
import com.example.hundredplaces.data.model.visit.repositories.VisitRepository
import com.example.hundredplaces.data.services.distance.DistanceService
import com.example.hundredplaces.data.services.distance.DefaultDistanceService
import com.example.hundredplaces.data.services.landmark.LandmarkRestApi
import com.example.hundredplaces.data.services.landmark.LandmarkService
import com.example.hundredplaces.data.services.landmark.LandmarkServiceImpl
import com.example.hundredplaces.util.InstantConverter
import com.example.hundredplaces.util.NetworkConnection
import com.example.hundredplaces.workers.WorkManagerRepository
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

    override val userAppPreferencesRepository: UserAppPreferencesRepository by lazy {
        UserAppPreferencesRepository(context.dataStore)
    }

    override val workManagerRepository: WorkManagerRepository by lazy {
        WorkManagerRepository(context)
    }

    override val cityRepository: CityRepository by lazy {
        DefaultCityRepository(
            hundredPlacesLocalDatabase.cityDao(),
            retrofit.create(CityRestApi::class.java),
            networkConnection
        )
    }
    override val placeRepository: PlaceRepository by lazy {
        DefaultPlaceRepository(
            hundredPlacesLocalDatabase.placeDao(),
            retrofit.create(PlaceRestApi::class.java),
            networkConnection
        )
    }
    override val imageRepository: ImageRepository by lazy {
        DefaultImageRepository(
            hundredPlacesLocalDatabase.imageDao(),
            retrofit.create(ImageRestApi::class.java),
            networkConnection
        )
    }
    override val userRepository: UserRepository by lazy {
        DefaultUserRepository(
            hundredPlacesLocalDatabase.userDao(),
            retrofit.create(UserRestApi::class.java),
            networkConnection
        )
    }
    override val visitRepository: VisitRepository by lazy {
        DefaultVisitRepository(
            hundredPlacesLocalDatabase.visitDao(),
            retrofit.create(VisitRestApi::class.java),
            networkConnection
        )
    }

    override val usersPlacesPreferencesRepository: UsersPlacesPreferencesRepository by lazy {
        DefaultUsersPlacesPreferencesRepository(
            hundredPlacesLocalDatabase.usersPlacesPreferencesDao(),
            retrofit.create(UsersPlacesPreferencesRestApi::class.java),
            networkConnection
        )
    }

    override val landmarkService: LandmarkService by lazy {
        LandmarkServiceImpl(
            retrofit.create(LandmarkRestApi::class.java),
            networkConnection
        )
    }

    override val distanceService: DistanceService by lazy {
        DefaultDistanceService(
            placeRepository
        )
    }

}