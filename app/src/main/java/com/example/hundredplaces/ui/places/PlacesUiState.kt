package com.example.hundredplaces.ui.places

import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.google.android.gms.location.Geofence
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDateTime

data class PlacesUiState(
    val places: List<PlaceWithCityAndImages> = listOf(),
    val filteredPlaces: List<PlaceWithCityAndImages> = listOf(),
    val selectedPlaceId: Long = 0,
    val distances: Map<Long, Float> = mapOf(),
    val cameraPosition: CameraPosition = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 15f),
    val visits: List<LocalDateTime> = listOf(),
    val triggeredGeofences: List<Geofence> = listOf(),
    //val selectedFilterCategoriesSet: MutableSet<PlaceFilterCategoriesEnum> = mutableSetOf()
)
