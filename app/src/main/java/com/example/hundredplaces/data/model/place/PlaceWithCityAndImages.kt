package com.example.hundredplaces.data.model.place

import androidx.room.Embedded
import androidx.room.Relation
import com.example.hundredplaces.data.model.city.City
import com.example.hundredplaces.data.model.image.Image
import kotlinx.serialization.Serializable

@Serializable
data class PlaceWithCityAndImages(
    @Embedded
    val place: Place,
    @Relation(
        parentColumn = "city_id",
        entityColumn = "id",
        projection = ["name"],
        entity = City::class
        )
    val city: String,
    @Relation(
        parentColumn = "id",
        entityColumn = "place_id",
        projection = ["image_path"],
        entity = Image::class
    )
    val images: List<String>
)
