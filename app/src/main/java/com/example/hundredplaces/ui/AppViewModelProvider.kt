package com.example.hundredplaces.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.hundredplaces.HundredPlacesApplication
import com.example.hundredplaces.ui.account.AccountViewModel
import com.example.hundredplaces.ui.components.AppContentViewModel
import com.example.hundredplaces.ui.places.PlacesViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for AppContentViewModel
        initializer {
            AppContentViewModel(
                hundredPlacesApplication().container.userPreferencesRepository
            )
        }
        // Initializer for PlacesViewModel
        initializer {
            PlacesViewModel(
                hundredPlacesApplication().container.placesRepository,
                hundredPlacesApplication().container.visitsRepository
            )
        }
        // Initializer for AccountViewModel
        initializer {
            AccountViewModel(
                hundredPlacesApplication().container.usersRepository,
                hundredPlacesApplication().container.userPreferencesRepository
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
