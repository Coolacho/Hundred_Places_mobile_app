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
    val Factory = viewModelFactory {
        // Initializer for AppContentViewModel
        initializer {
            AppContentViewModel (
                hundredPlacesApplication().container.userPreferencesRepository
            )
        }
        // Initializer for PlacesViewModel
        initializer {
            PlacesViewModel (
                hundredPlacesApplication().container.placesRepository,
                hundredPlacesApplication().container.usersPlacesPreferencesDataRepository,
                createSavedStateHandle()
            )
        }
        initializer {
            PlaceDetailsViewModel (
                hundredPlacesApplication().container.placesRepository,
                hundredPlacesApplication().container.visitsRepository,
                createSavedStateHandle()
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
                hundredPlacesApplication().container.userPreferencesRepository,
                hundredPlacesApplication().container.workManagerRepository,
                createSavedStateHandle()
            )
        }
        // Initializer for AchievementsViewModel
        initializer {
            AchievementsViewModel (
                hundredPlacesApplication().container.visitsRepository,
                createSavedStateHandle()
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
