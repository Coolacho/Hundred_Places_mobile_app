package com.example.hundredplaces.ui.placeDetails

import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import java.time.Instant

sealed interface PlaceDetailsUiState {
    data class Success(
        val place: PlaceWithCityAndImages,
        val descriptionText: String? = null,
        val visits: List<Instant> = emptyList(),
    ) : PlaceDetailsUiState
    data object Error : PlaceDetailsUiState
    data object Loading : PlaceDetailsUiState
}
