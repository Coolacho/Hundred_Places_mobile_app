package com.example.hundredplaces.data.containers

import com.example.hundredplaces.data.UserAppPreferencesRepository
import com.example.hundredplaces.workers.WorkManagerRepository
import com.example.hundredplaces.data.model.city.repositories.CitiesRepository
import com.example.hundredplaces.data.model.image.repositories.ImagesRepository
import com.example.hundredplaces.data.model.place.repositories.PlacesRepository
import com.example.hundredplaces.data.model.usersPlacesPreferences.repositories.UsersPlacesPreferencesRepository
import com.example.hundredplaces.data.model.user.repositories.UsersRepository
import com.example.hundredplaces.data.model.visit.repositories.VisitsRepository
import com.example.hundredplaces.util.NetworkConnection

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val userAppPreferencesRepository: UserAppPreferencesRepository
    val workManagerRepository: WorkManagerRepository
    val networkConnection: NetworkConnection
    val citiesRepository: CitiesRepository
    val placesRepository: PlacesRepository
    val imagesRepository: ImagesRepository
    val usersRepository: UsersRepository
    val visitsRepository: VisitsRepository
    val usersPlacesPreferencesDataRepository: UsersPlacesPreferencesRepository
}