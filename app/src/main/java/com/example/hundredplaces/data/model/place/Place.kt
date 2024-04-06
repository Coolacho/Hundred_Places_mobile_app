package com.example.hundredplaces.data.model.place

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.hundredplaces.data.model.city.City
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "places",
    indices = [
        Index(
            value = ["name"],
            unique = true
        )
    ],
    foreignKeys = [
        ForeignKey(
            entity = City::class,
            parentColumns = ["city_id"],
            childColumns = ["id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class Place(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private val id: Int = 0,
    @ColumnInfo(name = "name")
    private val name: String,
    @ColumnInfo(name = "latitude")
    private val latitude: Double,
    @ColumnInfo(name = "longitude")
    private val longitude: Double,
    @ColumnInfo(name = "rating")
    private val rating: Double,
    @ColumnInfo(name = "description_path")
    private val descriptionPath: String,
    @ColumnInfo(name = "is_100_places")
    private val is100Places: Boolean,
    @ColumnInfo(name = "type")
    private val type: PlaceTypeEnum,
    @ColumnInfo(name = "city_id")
    private val cityId: Int
)
