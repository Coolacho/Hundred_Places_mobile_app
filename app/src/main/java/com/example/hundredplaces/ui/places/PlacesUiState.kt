package com.example.hundredplaces.ui.places

import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages

data class PlacesUiState(
    val places: List<PlaceWithCityAndImages> = listOf(),
    val currentSelectedPlace: PlaceWithCityAndImages? = null,
)
