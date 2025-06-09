package com.example.hundredplaces.data.model.place

import androidx.room.ColumnInfo

data class PlaceCoordinates(
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double
)
