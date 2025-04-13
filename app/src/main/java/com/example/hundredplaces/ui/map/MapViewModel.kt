package com.example.hundredplaces.ui.map

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hundredplaces.data.model.place.repositories.PlacesRepository
import com.example.hundredplaces.workers.WorkManagerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MapViewModel(
    placesRepository: PlacesRepository,
    private val workManagerRepository: WorkManagerRepository
) : ViewModel() {

    private val _places = placesRepository.allPlacesWithCityAndImages
    private val _distances = MutableStateFlow(mapOf<Long, Float>())

    val uiState = combine(
        _places, _distances
    ) { places, distances ->
        workManagerRepository.addGeofences() /*TODO:
                                                1. Instead of passing places as an argument,
                                                make the work collect the places separately
                                                and add geofences independently (done)
                                                2. Move the call to the MainActivity or other independent place
                                                */
        MapUiState(
            places = places,
            distances = distances
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MapUiState()
        )

    fun getDistances(location: Location) {
        _distances.value = buildMap {
            uiState.value.places.forEach {
                val placeLocation = Location("")
                placeLocation.latitude = it.place.latitude
                placeLocation.longitude = it.place.longitude
                put(it.place.id, location.distanceTo(placeLocation))
            }
        }
    }
}