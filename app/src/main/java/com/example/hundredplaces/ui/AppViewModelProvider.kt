package com.example.hundredplaces.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.hundredplaces.HundredPlacesApplication
import com.example.hundredplaces.ui.account.AccountViewModel
import com.example.hundredplaces.ui.achievements.AchievementsViewModel
import com.example.hundredplaces.ui.camera.CameraViewModel
import com.example.hundredplaces.ui.components.AppContentViewModel
import com.example.hundredplaces.ui.login.LoginViewModel
import com.example.hundredplaces.ui.map.MapViewModel
import com.example.hundredplaces.ui.placeDetails.PlaceDetailsViewModel
import com.example.hundredplaces.ui.places.PlacesViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {

    val PLACE_ID_KEY = object : CreationExtras.Key<Long> {}

    val Factory = viewModelFactory {
        initializer {
            AppContentViewModel (
                hundredPlacesApplication().container.userAppPreferencesRepository
            )
        }
        initializer {
            PlacesViewModel (
                hundredPlacesApplication().container.placeRepository,
                hundredPlacesApplication().container.cityRepository,
                hundredPlacesApplication().container.imageRepository,
                hundredPlacesApplication().container.userRepository,
                hundredPlacesApplication().container.usersPlacesPreferencesRepository,
                hundredPlacesApplication().container.networkConnection
            )
        }
        initializer {
            PlaceDetailsViewModel (
                hundredPlacesApplication().container.placeRepository,
                hundredPlacesApplication().container.userRepository,
                hundredPlacesApplication().container.visitRepository,
                this[PLACE_ID_KEY] as Long
            )
        }
        initializer {
            MapViewModel (
                hundredPlacesApplication().container.placeRepository,
                hundredPlacesApplication().container.distanceService
            )
        }
        initializer {
            AccountViewModel (
                hundredPlacesApplication().container.userRepository,
                hundredPlacesApplication().container.userAppPreferencesRepository,
            )
        }
        initializer {
            AchievementsViewModel (
                hundredPlacesApplication().container.visitRepository,
                hundredPlacesApplication().container.userRepository
            )
        }
        initializer {
            CameraViewModel(
                hundredPlacesApplication().container.landmarkService,
                hundredPlacesApplication().container.placeRepository
            )
        }
        initializer {
            LoginViewModel(
                hundredPlacesApplication().container.userRepository,
                hundredPlacesApplication().container.userAppPreferencesRepository,
                hundredPlacesApplication().container.imageRepository
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [HundredPlacesApplication].
 */
fun CreationExtras.hundredPlacesApplication(): HundredPlacesApplication =
    (this[APPLICATION_KEY] as HundredPlacesApplication)
