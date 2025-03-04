package com.example.hundredplaces.data.model.usersPlacesPreferences

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.hundredplaces.data.model.place.Place
import com.example.hundredplaces.data.model.user.User
import java.time.Instant

@Entity(
    tableName = "users_places_preferences",
    primaryKeys = [
        "user_id",
        "place_id"
    ],
    indices = [
        Index(
            value = ["user_id"]
        ),
        Index(
            value = ["place_id"]
        )
    ],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Place::class,
            parentColumns = ["id"],
            childColumns = ["place_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UsersPlacesPreferences(
    @ColumnInfo(name = "user_id")
    val userId: Long,
    @ColumnInfo(name = "place_id")
    val placeId: Long,
    @ColumnInfo(name = "rating")
    val rating: Double,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean,
    @ColumnInfo(name = "last_update")
    val lastUpdate: Instant
)
