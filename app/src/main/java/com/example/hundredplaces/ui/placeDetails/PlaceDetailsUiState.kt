package com.example.hundredplaces.ui.placeDetails

import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import java.time.Instant

data class PlaceDetailsUiState(
    val place: PlaceWithCityAndImages? = null,
    val descriptionText: String? = null,
    val visits: List<Instant> = emptyList(),
)
