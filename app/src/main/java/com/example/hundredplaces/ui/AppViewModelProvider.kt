package com.example.hundredplaces.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.hundredplaces.HundredPlacesApplication
import com.example.hundredplaces.ui.account.AccountViewModel
import com.example.hundredplaces.ui.achievements.AchievementsViewModel
import com.example.hundredplaces.ui.components.AppContentViewModel
import com.example.hundredplaces.ui.map.MapViewModel
import com.example.hundredplaces.ui.placeDetails.PlaceDetailsViewModel
import com.example.hundredplaces.ui.places.PlacesViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {

    val PLACE_ID_KEY = object : CreationExtras.Key<Long> {}

    val Factory = viewModelFactory {
        // Initializer for AppContentViewModel
        initializer {
            AppContentViewModel (
                hundredPlacesApplication().container.userAppPreferencesRepository
            )
        }
        // Initializer for PlacesViewModel
        initializer {
            PlacesViewModel (
                hundredPlacesApplication().container.placesRepository,
                hundredPlacesApplication().container.usersRepository,
                hundredPlacesApplication().container.usersPlacesPreferencesDataRepository,
            )
        }
        initializer {
            PlaceDetailsViewModel (
                hundredPlacesApplication().container.placesRepository,
                hundredPlacesApplication().container.usersRepository,
                hundredPlacesApplication().container.visitsRepository,
                this[PLACE_ID_KEY] as Long
            )
        }
        initializer {
            MapViewModel (
                hundredPlacesApplication().container.placesRepository,
                hundredPlacesApplication().container.workManagerRepository
            )
        }
        // Initializer for AccountViewModel
        initializer {
            AccountViewModel (
                hundredPlacesApplication().container.usersRepository,
                hundredPlacesApplication().container.userAppPreferencesRepository,
                hundredPlacesApplication().container.workManagerRepository,
                createSavedStateHandle()
            )
        }
        // Initializer for AchievementsViewModel
        initializer {
            AchievementsViewModel (
                hundredPlacesApplication().container.visitsRepository,
                hundredPlacesApplication().container.usersRepository
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
