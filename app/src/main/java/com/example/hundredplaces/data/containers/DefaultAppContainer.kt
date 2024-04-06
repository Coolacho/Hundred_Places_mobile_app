package com.example.hundredplaces.data.containers

import android.content.Context
import com.example.hundredplaces.data.LocalDatabase
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
import com.example.hundredplaces.util.NetworkConnection
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.create

/**
 * [AppContainer] implementation that provides instances of repositories
 */
class DefaultAppContainer(
    private val context: Context
) : AppContainer{

    private val baseUrl =
        "http://" //TODO complete the url
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val localDatabase by lazy {
        LocalDatabase.getDatabase(context)
    }

    override val networkConnection by lazy {
        NetworkConnection(context)
    }

    override val citiesRepository: CitiesDataRepository by lazy {
        CitiesDataRepository(
            CitiesLocalRepository(localDatabase.cityDao()),
            CitiesRemoteRepository(retrofit.create(CityRestApiService::class.java)),
            networkConnection
        )
    }
    override val placesRepository: PlacesDataRepository by lazy {
        PlacesDataRepository(
            PlacesLocalRepository(localDatabase.placeDao()),
            PlacesRemoteRepository(retrofit.create(PlaceRestApiService::class.java)),
            networkConnection
        )
    }
    override val imagesRepository: ImagesDataRepository by lazy {
        ImagesDataRepository(
            ImagesLocalRepository(localDatabase.imageDao()),
            ImagesRemoteRepository(retrofit.create(ImageRestApiService::class.java)),
            networkConnection
        )
    }
    override val usersRepository: UsersDataRepository by lazy {
        UsersDataRepository(
            UsersLocalRepository(localDatabase.userDao()),
            UsersRemoteRepository(retrofit.create(UserRestApiService::class.java)),
            networkConnection
        )
    }
    override val visitsRepository: VisitsDataRepository by lazy {
        VisitsDataRepository(
            VisitsLocalRepository(localDatabase.visitDao()),
            VisitsRemoteRepository(retrofit.create(VisitRestApiService::class.java)),
            networkConnection
        )
    }
}