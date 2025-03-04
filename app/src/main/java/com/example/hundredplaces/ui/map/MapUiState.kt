package com.example.hundredplaces.ui.map

import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState

data class MapUiState (
    val places: List<PlaceWithCityAndImages> = emptyList(),
    val cameraPositionState: CameraPositionState = CameraPositionState(CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 15f))
)