package com.example.hundredplaces.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hundredplaces.data.model.place.repositories.PlaceRepository
import com.example.hundredplaces.data.services.distance.DistanceService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MapViewModel(
    placeRepository: PlaceRepository,
    distanceService: DistanceService
) : ViewModel() {

    private val _places = placeRepository.allPlacesWithCityAndImages
    private val _distances = distanceService.distances

    val uiState = combine(
        _places, _distances
    ) { places, distances ->
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

}