package com.example.hundredplaces.ui.map

import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages

data class MapUiState (
    val places: List<PlaceWithCityAndImages> = emptyList(),
    val distances: Map<Long, Float> = emptyMap(),
)