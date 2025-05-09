package com.example.hundredplaces.ui.places

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSliderState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.example.hundredplaces.data.model.place.repositories.PlacesRepository
import com.example.hundredplaces.data.model.user.repositories.UsersRepository
import com.example.hundredplaces.data.model.usersPlacesPreferences.UsersPlacesPreferences
import com.example.hundredplaces.data.model.usersPlacesPreferences.repositories.UsersPlacesPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
class PlacesViewModel(
    placesRepository: PlacesRepository,
    usersRepository: UsersRepository,
    private val usersPlacesPreferencesRepository: UsersPlacesPreferencesRepository,
) : ViewModel() {

    private val _userId = usersRepository.userId

    private val _searchText = MutableStateFlow("")
    private val _isFilterScreenOpen: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _filtersSet: MutableStateFlow<Set<PlaceFiltersEnum>> = MutableStateFlow(emptySet())
    private val _rangeSliderState: MutableStateFlow<RangeSliderState> = MutableStateFlow(
        RangeSliderState(
            activeRangeStart = 0f,
            activeRangeEnd = 5f,
            steps = 49,
            valueRange = 0f..5f
        ))
    private val _favorites = _userId.flatMapLatest { userId ->
        userId?.let { usersPlacesPreferencesRepository.getFavoritePlacesByUserId(userId) } ?: flowOf(emptyList())
    }
    private val _ratings = _userId.flatMapLatest { userId ->
        userId?.let { usersPlacesPreferencesRepository.getPlacesRatingsByUserId(userId) } ?: flowOf(emptyMap())
    }
    private val _filteredPlaces = combine(
        placesRepository.allPlacesWithCityAndImages,
        _filtersSet,
        _favorites,
        _rangeSliderState,
        _searchText
    ) { allPlaces, filtersSet, favorites, rangeSliderState, searchText ->
        allPlaces.filter { place ->
            // Start by assuming true (no filters apply if filtersSet is empty)
            var shouldInclude = true

            // Filter for 100 places if applicable
            if (filtersSet.contains(PlaceFiltersEnum.HUNDRED_PLACES)) {
                shouldInclude = shouldInclude && place.place.is100Places
            }

            // Filter for favorites (currently hardcoded to true; change as needed)
            if (filtersSet.contains(PlaceFiltersEnum.FAVORITES)) {
                shouldInclude = shouldInclude && favorites.contains(place.place.id)
            }

            // Filter for rating range if applicable
            if (filtersSet.contains(PlaceFiltersEnum.RATING)) {
                shouldInclude = shouldInclude && (place.place.rating in rangeSliderState.activeRangeStart..rangeSliderState.activeRangeEnd)
            }

            // Check if there are any other filter categories to apply
            val otherCategories = filtersSet.filterNot {
                it == PlaceFiltersEnum.HUNDRED_PLACES ||
                        it == PlaceFiltersEnum.FAVORITES ||
                        it == PlaceFiltersEnum.RATING
            }

            // Apply the filter for other categories based on place type
            if (otherCategories.isNotEmpty()) {
                shouldInclude = shouldInclude && filtersSet.contains(PlaceFiltersEnum.valueOf(place.place.type.name))
            }

            shouldInclude = shouldInclude && place.place.name.contains(searchText, ignoreCase = true)

            shouldInclude
        }
    }

    val uiState = combine(
        _searchText, _filteredPlaces, _ratings, _favorites, _isFilterScreenOpen, _filtersSet, _rangeSliderState
    ) { result ->
        val searchText = result[0] as String
        val filteredPlaces = result[1] as List<PlaceWithCityAndImages>
        val ratings = result[2] as Map<Long, Double>
        val favorites = result[3] as List<Long>
        val isFilterScreenOpen = result[4] as Boolean
        val filtersSet = result[5] as Set<PlaceFiltersEnum>
        val rangeSliderState = result[6] as RangeSliderState
        rangeSliderState.onValueChangeFinished = { updateSliderState() }
        PlacesUiState(
            searchText = searchText,
            filteredPlaces = filteredPlaces,
            ratings = ratings,
            favorites = favorites,
            isFilterScreenOpen = isFilterScreenOpen,
            filtersSet = filtersSet,
            rangeSliderState = rangeSliderState
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PlacesUiState()
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

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

    fun toggleFavorite(placeId: Long, rating: Double, isFavorite: Boolean) {
        viewModelScope.launch {
            usersPlacesPreferencesRepository.upsert(
                UsersPlacesPreferences(_userId.value!!, placeId, rating, !isFavorite, Instant.now())
            )
        }
    }

    fun saveRating(placeId: Long, rating: Double, isFavorite: Boolean) {
        viewModelScope.launch {
            usersPlacesPreferencesRepository.upsert(
                UsersPlacesPreferences(_userId.value!!, placeId, rating, isFavorite, Instant.now())
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