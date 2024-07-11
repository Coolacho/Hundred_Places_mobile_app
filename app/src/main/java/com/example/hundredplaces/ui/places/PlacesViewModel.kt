package com.example.hundredplaces.ui.places

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hundredplaces.data.WorkManagerRepository
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.example.hundredplaces.data.model.place.repositories.PlacesDataRepository
import com.example.hundredplaces.data.model.user.User
import com.example.hundredplaces.data.model.visit.Visit
import com.example.hundredplaces.data.model.visit.repositories.VisitsDataRepository
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class PlacesViewModel(
    private val placesDataRepository: PlacesDataRepository,
    private val visitsDataRepository: VisitsDataRepository,
    private val workManagerRepository: WorkManagerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlacesUiState())
    val uiState: StateFlow<PlacesUiState> = _uiState

    init {
        getPlaces()
    }

    fun selectPlaceCard(placeWithCityAndImages: PlaceWithCityAndImages) {
        _uiState.update {
            it.copy(
                selectedPlace = placeWithCityAndImages
            )
        }
    }

    fun selectDetailsTab(index: Int) {
        _uiState.update {
            it.copy(
                selectedDetailsTab = index
            )
        }
    }

    private fun getPlaces() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    places = placesDataRepository.getAllPlacesWithCityAndImages()
                )
            }
            workManagerRepository.addGeofences(uiState.value.places)
        }
    }

    fun getDistances(location: Location) {
        val distances: MutableMap<Long, Int> = mutableMapOf()
        uiState.value.places.forEach {
            val placeLocation = Location("")
            placeLocation.latitude = it.place.latitude
            placeLocation.longitude = it.place.longitude
            distances[it.place.id] = location.distanceTo(placeLocation).toInt()
        }
        _uiState.update {
            it.copy(
                distances = distances
            )
        }
    }

    fun updateCameraPosition(location: Location) {
        _uiState.update {
            it.copy(
                cameraPosition = CameraPosition.fromLatLngZoom(
                    LatLng(
                        location.latitude,
                        location.longitude
                    ), 15f
                )
            )
        }
    }

    fun addVisit(user: User) {
        viewModelScope.launch {
            visitsDataRepository.insertVisit(
                Visit(
                    userId = user.id,
                    placeId = uiState.value.selectedPlace.place.id,
                    dateVisited = LocalDateTime.now()
                )
            )
        }
    }

    fun getVisitsByUserAndPlace(user: User) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    visits = visitsDataRepository.getAllVisitDatesByUserIdAndPlaceId(
                        user.id,
                        uiState.value.selectedPlace.place.id
                    )
                )
            }
        }
    }

}