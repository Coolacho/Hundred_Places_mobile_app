package com.example.hundredplaces.data.containers

import com.example.hundredplaces.data.services.landmark.LandmarkService
import com.example.hundredplaces.data.UserAppPreferencesRepository
import com.example.hundredplaces.workers.WorkManagerRepository
import com.example.hundredplaces.data.model.city.repositories.CityRepository
import com.example.hundredplaces.data.model.image.repositories.ImageRepository
import com.example.hundredplaces.data.model.place.repositories.PlaceRepository
import com.example.hundredplaces.data.model.usersPlacesPreferences.repositories.UsersPlacesPreferencesRepository
import com.example.hundredplaces.data.model.user.repositories.UserRepository
import com.example.hundredplaces.data.model.visit.repositories.VisitRepository
import com.example.hundredplaces.data.services.distance.DistanceService
import com.example.hundredplaces.util.NetworkConnection

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val userAppPreferencesRepository: UserAppPreferencesRepository
    val workManagerRepository: WorkManagerRepository
    val networkConnection: NetworkConnection
    val cityRepository: CityRepository
    val placeRepository: PlaceRepository
    val imageRepository: ImageRepository
    val userRepository: UserRepository
    val visitRepository: VisitRepository
    val usersPlacesPreferencesRepository: UsersPlacesPreferencesRepository
    val landmarkService: LandmarkService
    val distanceService: DistanceService
}