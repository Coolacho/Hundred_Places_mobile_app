package com.example.hundredplaces.ui.places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.example.hundredplaces.data.model.place.repositories.PlacesDataRepository
import com.example.hundredplaces.data.model.user.User
import com.example.hundredplaces.data.model.visit.Visit
import com.example.hundredplaces.data.model.visit.repositories.VisitsDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class PlacesViewModel (
    private val placesDataRepository: PlacesDataRepository,
    private val visitsDataRepository: VisitsDataRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlacesUiState())
    val uiState: StateFlow<PlacesUiState> = _uiState

    init {
        getPlaces()
    }

    fun selectPlaceCard(placeWithCityAndImages: PlaceWithCityAndImages) {
        _uiState.update {
            it.copy(
                currentSelectedPlace = placeWithCityAndImages
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
        }
    }

    fun addVisit(user: User) {
        viewModelScope.launch {
            visitsDataRepository.insertVisit(Visit(userId = user.id, placeId = uiState.value.currentSelectedPlace.place.id, dateVisited = LocalDateTime.now()))
        }
    }

    fun getVisitsByUserIdAndPlaceId(user: User) {
        viewModelScope.launch {
            visitsDataRepository.getAllVisitDatesByUserIdAndPlaceId(user.id, uiState.value.currentSelectedPlace.place.id)
        }
    }

}