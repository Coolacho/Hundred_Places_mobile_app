package com.example.hundredplaces.ui.places

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSliderState
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages

data class PlacesUiState @OptIn(ExperimentalMaterial3Api::class) constructor(
    val searchText: String = "",
    val filteredPlaces: List<PlaceWithCityAndImages> = emptyList(),
    val ratings: Map<Long, Double> = emptyMap(),
    val favorites: List<Long> = emptyList(),
    val isFilterScreenOpen: Boolean = false,
    val filtersSet: Set<PlaceFiltersEnum> = emptySet(),
    val rangeSliderState: RangeSliderState = RangeSliderState(
        activeRangeStart = 0f,
        activeRangeEnd = 5f,
        steps = 49,
        valueRange = 0f..5f
    ),
    val isRefreshing: Boolean = false,
)
