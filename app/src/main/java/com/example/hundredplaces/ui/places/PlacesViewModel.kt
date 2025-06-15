package com.example.hundredplaces.ui.places

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSliderState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hundredplaces.data.model.city.repositories.CityRepository
import com.example.hundredplaces.data.model.image.repositories.ImageRepository
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.example.hundredplaces.data.model.place.repositories.PlaceRepository
import com.example.hundredplaces.data.model.user.repositories.UserRepository
import com.example.hundredplaces.data.model.usersPlacesPreferences.UsersPlacesPreferences
import com.example.hundredplaces.data.model.usersPlacesPreferences.repositories.UsersPlacesPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
class PlacesViewModel(
    private val placeRepository: PlaceRepository,
    private val cityRepository: CityRepository,
    private val imageRepository: ImageRepository,
    userRepository: UserRepository,
    private val usersPlacesPreferencesRepository: UsersPlacesPreferencesRepository,
) : ViewModel() {

    private val _userId = userRepository.userId

    private val _places = placeRepository.getAllPlacesWithCityAndImages()
    private val _isRefreshing = MutableStateFlow(false)
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
        _places,
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
        _searchText, _isRefreshing, _isFilterScreenOpen, _filtersSet, _rangeSliderState, _favorites, _ratings, _filteredPlaces
    ) { result ->
        val searchText = result[0] as String
        val isRefreshing = result[1] as Boolean
        val isFilterScreenOpen = result[2] as Boolean
        val filtersSet = result[3] as Set<PlaceFiltersEnum>
        val rangeSliderState = result[4] as RangeSliderState
        val favorites = result[5] as List<Long>
        val ratings = result[6] as Map<Long, Double>
        val filteredPlaces = result[7] as List<PlaceWithCityAndImages>
        rangeSliderState.onValueChangeFinished = { updateSliderState() }
        PlacesUiState(
            searchText = searchText,
            filteredPlaces = filteredPlaces,
            ratings = ratings,
            favorites = favorites,
            isFilterScreenOpen = isFilterScreenOpen,
            filtersSet = filtersSet,
            rangeSliderState = rangeSliderState,
            isRefreshing = isRefreshing
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

    fun refresh() {
        _isRefreshing.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                cityRepository.pullCities()
                placeRepository.pullPlaces()
                imageRepository.pullImages()
            }
            catch (_: SocketTimeoutException) {
                Log.e("Static data refresh", "Server is not reachable.")
            }
            _isRefreshing.value = false
        }
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