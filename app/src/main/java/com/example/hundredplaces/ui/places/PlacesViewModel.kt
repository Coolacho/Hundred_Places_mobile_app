package com.example.hundredplaces.ui.places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.example.hundredplaces.data.model.place.repositories.PlacesDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlacesViewModel (
    private val placesDataRepository: PlacesDataRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlacesUiState())
    val uiState: StateFlow<PlacesUiState> = _uiState

    init {
        //getPlaces()
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
            placesDataRepository.getAllPlacesStream().collect { places ->
                _uiState.update {
                    it.copy(
                        places = places
                    )
                }
            }

        }
    }

}