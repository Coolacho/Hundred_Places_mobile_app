package com.example.hundredplaces.ui.places

import com.example.hundredplaces.data.model.place.Place
import com.example.hundredplaces.data.model.place.PlaceTypeEnum
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.google.android.gms.location.Geofence

data class PlacesUiState(
    val places: List<PlaceWithCityAndImages> = listOf(),
    val currentSelectedPlace: PlaceWithCityAndImages = PlaceWithCityAndImages(
        place = Place(name = "", latitude = 0.0, longitude = 0.0, rating = 0.0, descriptionPath = "", is100Places = false, type = PlaceTypeEnum.OTHER, cityId = 0),
        city = "",
        images = listOf()
    ),
    val triggeredGeofences: List<Geofence> = listOf()
)
