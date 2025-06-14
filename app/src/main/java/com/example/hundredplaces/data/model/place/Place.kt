package com.example.hundredplaces.data.model.place

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.hundredplaces.data.model.city.City
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "places",
    indices = [
        Index(
            value = ["city_id"]
        )
    ],
    foreignKeys = [
        ForeignKey(
            entity = City::class,
            parentColumns = ["id"],
            childColumns = ["city_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
            deferred = true
        )
    ]
)
data class Place(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double,
    @ColumnInfo(name = "rating")
    val rating: Double,
    @ColumnInfo(name = "description_path")
    val descriptionPath: String,
    @ColumnInfo(name = "is_100_places")
    val is100Places: Boolean,
    @ColumnInfo(name = "type")
    val type: PlaceTypeEnum,
    @ColumnInfo(name = "city_id")
    val cityId: Long
) : Parcelable
