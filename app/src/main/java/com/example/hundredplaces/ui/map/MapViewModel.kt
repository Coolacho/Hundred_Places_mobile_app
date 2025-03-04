package com.example.hundredplaces.ui.map

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hundredplaces.data.model.place.repositories.PlacesRepository
import com.example.hundredplaces.workers.WorkManagerRepository
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class MapViewModel(
    placesRepository: PlacesRepository,
    private val workManagerRepository: WorkManagerRepository
) : ViewModel() {

    private val _places = placesRepository.allPlacesWithCityAndImages
    private val _cameraPositionState: MutableStateFlow<CameraPositionState> = MutableStateFlow(CameraPositionState(CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 15f)))

    val uiState = combine(
        _places, _cameraPositionState
    ) { places, cameraPositionState ->
        workManagerRepository.addGeofences() /*TODO:
                                                1. Instead of passing places as an argument,
                                                make the work collect the places separately
                                                and add geofences independently (done)
                                                2. Move the call to the MainActivity or other independent place
                                                */
        MapUiState(
            places = places,
            cameraPositionState = cameraPositionState
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MapUiState()
        )

    fun updateCameraPositionState(location: Location) {
        _cameraPositionState.update {
            CameraPositionState(
                CameraPosition.fromLatLngZoom(
                    LatLng(
                        location.latitude,
                        location.longitude
                    ), it.position.zoom
                ))
        }
    }
}