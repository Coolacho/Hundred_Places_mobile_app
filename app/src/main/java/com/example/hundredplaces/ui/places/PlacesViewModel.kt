package com.example.hundredplaces.ui.places

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hundredplaces.data.WorkManagerRepository
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.example.hundredplaces.data.model.place.repositories.PlacesDataRepository
import com.example.hundredplaces.data.model.user.User
import com.example.hundredplaces.data.model.visit.Visit
import com.example.hundredplaces.data.model.visit.repositories.VisitsDataRepository
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class PlacesViewModel(
    private val placesDataRepository: PlacesDataRepository,
    private val visitsDataRepository: VisitsDataRepository,
    private val workManagerRepository: WorkManagerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlacesUiState())
    val uiState: StateFlow<PlacesUiState> = _uiState

    init {
        getPlaces()
    }

    fun getSelectedPlace(): PlaceWithCityAndImages {
        return uiState.value.places.find { it.place.id == uiState.value.selectedPlaceId } ?: throw NoSuchElementException("No object found with $uiState.value.selectedPlaceId")
    }

    fun selectPlace(placeId: Long) {
        _uiState.update {
            it.copy(
                selectedPlaceId = placeId
            )
        }
    }

    private fun getPlaces() {
        viewModelScope.launch {
            val places = placesDataRepository.getAllPlacesWithCityAndImages()
            _uiState.update {
                it.copy(
                    places = places,
                    filteredPlaces = places
                )
            }
            workManagerRepository.addGeofences(uiState.value.places)
        }
    }

    fun filterPlaces (
        filterList: List<PlaceFilterCategoriesEnum>,
        rangeStart: Float,
        rangeEnd: Float
    ) {
        _uiState.update {
            it.copy(
                filteredPlaces = it.places.filter { place ->
                    // Start by assuming true (no filters apply if filterList is empty)
                    var shouldInclude = true

                    // Filter for 100 places if applicable
                    if (filterList.contains(PlaceFilterCategoriesEnum.HUNDRED_PLACES)) {
                        shouldInclude = shouldInclude && place.place.is100Places
                    }

                    // Filter for favorites (currently hardcoded to true; change as needed)
                    if (filterList.contains(PlaceFilterCategoriesEnum.FAVORITES)) {
                        shouldInclude = shouldInclude && true // TODO: Implement favorite filtering logic
                    }

                    // Filter for rating range if applicable
                    if (filterList.contains(PlaceFilterCategoriesEnum.RATING)) {
                        shouldInclude = shouldInclude && (place.place.rating in rangeStart..rangeEnd)
                    }

                    // Check if there are any other filter categories to apply
                    val otherCategories = filterList.filterNot {
                        it == PlaceFilterCategoriesEnum.HUNDRED_PLACES ||
                        it == PlaceFilterCategoriesEnum.FAVORITES ||
                        it == PlaceFilterCategoriesEnum.RATING
                    }

                    // Apply the filter for other categories based on place type
                    if (otherCategories.isNotEmpty()) {
                        shouldInclude = shouldInclude && filterList.contains(PlaceFilterCategoriesEnum.valueOf(place.place.type.name))
                    }

                    shouldInclude
                }
            )
        }
    }

    fun getDistances(location: Location) {
        val distances: MutableMap<Long, Float> = mutableMapOf()
        uiState.value.places.forEach {
            val placeLocation = Location("")
            placeLocation.latitude = it.place.latitude
            placeLocation.longitude = it.place.longitude
            distances[it.place.id] = location.distanceTo(placeLocation)
        }
        _uiState.update {
            it.copy(
                distances = distances
            )
        }
    }

    fun updateCameraPosition(location: Location) {
        _uiState.update {
            it.copy(
                cameraPosition = CameraPosition.fromLatLngZoom(
                    LatLng(
                        location.latitude,
                        location.longitude
                    ), 15f
                )
            )
        }
    }

    fun addVisit(user: User) {
        viewModelScope.launch {
            visitsDataRepository.insertVisit(
                Visit(
                    userId = user.id,
                    placeId = uiState.value.selectedPlaceId,
                    dateVisited = LocalDateTime.now()
                )
            )
        }
    }

//    fun addFilter(filter: PlaceFilterCategoriesEnum) {
//        uiState.value.selectedFilterCategoriesSet.add(filter)
//        _uiState.update {
//            it.copy(
//                selectedFilterCategoriesSet = it.selectedFilterCategoriesSet
//            )
//        }
//        updatePlaces()
//    }
//
//    fun removeFilter(filter: PlaceFilterCategoriesEnum) {
//        uiState.value.selectedFilterCategoriesSet.remove(filter)
//        _uiState.update {
//            it.copy(
//                selectedFilterCategoriesSet = it.selectedFilterCategoriesSet
//            )
//        }
//        updatePlaces()
//    }


    fun getVisitsByUserAndPlace(user: User) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    visits = visitsDataRepository.getAllVisitDatesByUserIdAndPlaceId(
                        user.id,
                        uiState.value.selectedPlaceId
                    )
                )
            }
        }
    }

}