package com.example.hundredplaces.ui.places

import com.example.hundredplaces.data.model.place.Place
import com.example.hundredplaces.data.model.place.PlaceTypeEnum
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.google.android.gms.location.Geofence
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDateTime

data class PlacesUiState(
    val places: List<PlaceWithCityAndImages> = listOf(),
    val selectedPlace: PlaceWithCityAndImages = PlaceWithCityAndImages(
        place = Place(
            name = "",
            latitude = 0.0,
            longitude = 0.0,
            rating = 0.0,
            descriptionPath = "",
            is100Places = false,
            type = PlaceTypeEnum.OTHER,
            cityId = 0
        ),
        city = "",
        images = listOf()
    ),
    val selectedDetailsTab: Int = 0,
    val distances: Map<Long, Int> = mapOf(),
    val cameraPosition: CameraPosition = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 15f),
    val visits: List<LocalDateTime> = listOf(),
    val triggeredGeofences: List<Geofence> = listOf()
)
