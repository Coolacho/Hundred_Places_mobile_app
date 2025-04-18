package com.example.hundredplaces.data.model.image

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.hundredplaces.data.model.place.Place

@Entity(
    tableName = "images",
    indices = [
        Index(
            value = ["image_path"],
            unique = true
        ),
        Index(
            value = ["place_id"]
        )
    ],
    foreignKeys = [
        ForeignKey(
            entity = Place::class,
            parentColumns = ["id"],
            childColumns = ["place_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
            deferred = true
        )
    ]
)
data class Image (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "image_path")
    val imagePath: String,
    @ColumnInfo(name = "place_id")
    val placeId: Long
)