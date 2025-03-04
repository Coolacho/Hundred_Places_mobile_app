package com.example.hundredplaces.ui.places

import android.location.Location
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSliderState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.example.hundredplaces.data.model.place.repositories.PlacesRepository
import com.example.hundredplaces.data.model.usersPlacesPreferences.UsersPlacesPreferences
import com.example.hundredplaces.data.model.usersPlacesPreferences.repositories.UsersPlacesPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
class PlacesViewModel(
    placesRepository: PlacesRepository,
    private val usersPlacesPreferencesRepository: UsersPlacesPreferencesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userId: Long = 1//savedStateHandle["CURRENT_USER"]!!

    private val _distances = MutableStateFlow(mapOf<Long, Float>())
    private val _favorites = usersPlacesPreferencesRepository.getFavoritePlacesByUserId(userId)
    private val _ratings = usersPlacesPreferencesRepository.getPlacesRatingsByUserId(userId)
    private val _isFilterScreenOpen: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _filtersSet: MutableStateFlow<Set<PlaceFiltersEnum>> = MutableStateFlow(emptySet())
    private val _rangeSliderState: MutableStateFlow<RangeSliderState> = MutableStateFlow(
        RangeSliderState(
            activeRangeStart = 0f,
            activeRangeEnd = 5f,
            steps = 49,
            valueRange = 0f..5f
        ))
    private val _filteredPlaces = combine(
        placesRepository.allPlacesWithCityAndImages,
        _filtersSet,
        _favorites,
        _rangeSliderState
    ) { allPlaces, filtersSet, favorites, rangeSliderState ->
        allPlaces.filter { place ->
            // Start by assuming true (no filters apply if filtersSet is empty)
            var shouldInclude = true

            // Filter for 100 places if applicable
            if (_filtersSet.value.contains(PlaceFiltersEnum.HUNDRED_PLACES)) {
                shouldInclude = shouldInclude && place.place.is100Places
            }

            // Filter for favorites (currently hardcoded to true; change as needed)
            if (_filtersSet.value.contains(PlaceFiltersEnum.FAVORITES)) {
                shouldInclude = shouldInclude && favorites.contains(place.place.id)
            }

            // Filter for rating range if applicable
            if (_filtersSet.value.contains(PlaceFiltersEnum.RATING)) {
                shouldInclude = shouldInclude && (place.place.rating in _rangeSliderState.value.activeRangeStart.._rangeSliderState.value.activeRangeEnd)
            }

            // Check if there are any other filter categories to apply
            val otherCategories = _filtersSet.value.filterNot {
                it == PlaceFiltersEnum.HUNDRED_PLACES ||
                        it == PlaceFiltersEnum.FAVORITES ||
                        it == PlaceFiltersEnum.RATING
            }

            // Apply the filter for other categories based on place type
            if (otherCategories.isNotEmpty()) {
                shouldInclude = shouldInclude && _filtersSet.value.contains(PlaceFiltersEnum.valueOf(place.place.type.name))
            }

            shouldInclude
        }
    }

    val uiState = combine(
        _filteredPlaces, _distances, _ratings, _favorites, _isFilterScreenOpen, _filtersSet, _rangeSliderState
    ) { result ->
        val filteredPlaces = result[0] as List<PlaceWithCityAndImages>
        val distances = result[1] as Map<Long, Float>
        val ratings = result[2] as Map<Long, Double>
        val favorites = result[3] as List<Long>
        val isFilterScreenOpen = result[4] as Boolean
        val filtersSet = result[5] as Set<PlaceFiltersEnum>
        val rangeSliderState = result[6] as RangeSliderState
        rangeSliderState.onValueChangeFinished = { updateSliderState() }
        PlacesUiState(
            filteredPlaces,
            distances,
            ratings,
            favorites,
            isFilterScreenOpen,
            filtersSet,
            rangeSliderState
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PlacesUiState()
        )

    fun toggleFilter(filter: PlaceFiltersEnum) {
        if (_filtersSet.value.contains(filter)) {
            _filtersSet.update { it.minus(filter) }
        }
        else {
            _filtersSet.update { it.plus(filter) }
        }
    }

    fun openFilterScreen() {
        _isFilterScreenOpen.update { !it }
    }

    fun getDistances(location: Location) {
        _distances.value = buildMap {
            uiState.value.filteredPlaces.forEach {
                val placeLocation = Location("")
                placeLocation.latitude = it.place.latitude
                placeLocation.longitude = it.place.longitude
                put(it.place.id, location.distanceTo(placeLocation))
            }
        }
    }

    fun toggleFavorite(placeId: Long, rating: Double, isFavorite: Boolean) {
        viewModelScope.launch {
            usersPlacesPreferencesRepository.upsert(
                UsersPlacesPreferences(userId, placeId, rating, !isFavorite, Instant.now())
            )
        }
    }

    fun saveRating(placeId: Long, rating: Double, isFavorite: Boolean) {
        viewModelScope.launch {
            usersPlacesPreferencesRepository.upsert(
                UsersPlacesPreferences(userId, placeId, rating, isFavorite, Instant.now())
            )
        }
    }

    private fun updateSliderState() {
        if (_rangeSliderState.value.activeRangeStart > 0f || _rangeSliderState.value.activeRangeEnd < 5f) {
            if (!_filtersSet.value.contains(PlaceFiltersEnum.RATING))
                _filtersSet.update { it.plus(PlaceFiltersEnum.RATING) }
        }
        else {
            _filtersSet.update { it.minus(PlaceFiltersEnum.RATING) }
        }
    }

}